package com.bizcreator.core.remoting;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.BizContext;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bizcreator.core.remoting.Invocation;
import com.bizcreator.core.remoting.ServiceDefinition;
//import com.bizcreator.core.security.JBossPrincipalManager;
import com.bizcreator.core.security.PrincipalManager;

import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.SessionContextImpl;
import com.bizcreator.core.entity.LoginSession;
import com.bizcreator.core.json.JSONConverter;
import com.bizcreator.core.json.Jsonizable;
import com.bizcreator.core.json.UtilDateSerializer;
import com.bizcreator.core.security.User;
import com.bizcreator.core.session.ServiceBase;
import com.bizcreator.util.ObjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.StringWriter;
import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;

//import org.jbpm.JbpmConfiguration;
//import org.jbpm.JbpmContext;

/**
 * This is the servlet that intercept all the client calls and transmits these to the EJBs.
 * All the client calls are Http POST calls that use url rewriting to keep the session id.
 * <p>
 * If the client uses authentication, it should call an authenticate(String login, String password)
 * on a session.
 */
public class HttpSessionServer
        extends HttpServlet {

    static Log log = LogFactory.getLog(HttpSessionServer.class);
    public final static String SERVICE = "service";
    public final static String METHOD = "mn";
    public final static String ARGS = "args";
    public final static String TYPES = "types";
    public final static String ERROR = "error";
    public final static String MESSAGE = "message";
    public final static String INVOKEINFO = "invokeinfo";
    public final static String STACK_TRACE = "stackTrace";
    public final static String VALUE = "value";
    public final static int NORMAL = 200;
    public final static String RESP_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/html; charset=GB2312";
    public static final String CONTENT_TYPE_BSF = "application/x-bsf";
    public final static String EAR_NAME = "rhinofield-portal/";

    //The cache of remote stateless services (statefull are saved in the user session)
    protected static Hashtable serviceCache = new Hashtable();

    //The index used as key for the statefull services
    private int _maxServiceIndex = 0;
    
    //The Initial context for EJB lookup
    private static Context namingContext;
    private static String namingContextProperties = null;

    //Used for the management of the client principal. This part is generally dependant of the
    //J2EE server. We use reflection to instantiate the principal manger corresponding to the
    //current server.
    private static PrincipalManager principalManager;
    private static String principalManagerClassName = null;
    private static final String STATEFULL_CACHE = "stafullCache";

    //服务类型
    private String serviceType;

    //调用计数
    private static long invokeCount = 0;

    /**
     * Uses the properties defined in the servlet environment to instantiate the
     * principalManager and the initial context properties.
     */
    @Override
    public void init() throws ServletException {
        super.init();

        //1. 从web.xml中读取naming context properties
        if (namingContextProperties == null) {
            namingContextProperties = getServletContext().getInitParameter("ejbContextProperties");
        }
        initEjbContext(namingContextProperties);

        //2. 从web.xml中读取principal manager class
        if (principalManagerClassName == null) {
            principalManagerClassName = getServletContext().getInitParameter("principalManagerClassName");
        }

        if (principalManagerClassName != null) {
            try {
                Class aClass = Thread.currentThread().
                        getContextClassLoader().loadClass(principalManagerClassName);

                principalManager = (PrincipalManager) aClass.getConstructor(
                        new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                throw new RuntimeException("Unable to create the Principal (" +
                        principalManagerClassName + ") : " + e.toString());
            }
        } else {
            //principalManager = new JBossPrincipalManager();
        }

        //3. 设置服务类型
        serviceType = getServletContext().getInitParameter(BizContext.SERVICE_PARAM_NAME); //在<context-param>中设置
        if (serviceType == null) {
            serviceType = BizContext.DEFAULT_SERVICE_TYPE;
        }

        //end_point 用于标识是在服务器还是在客户端
        System.getProperties().put("end_point", "server");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>HttpSessionServer</title></head>");
        out.println("<body bgcolor=\"#ffffff\">");
        out.println("<p>The servlet has received a " + request.getMethod() +
                ". This is the reply.</p>");
        out.println("<p>The servlet has received a " + request.getMethod() +
                ". This is the reply.</p>");
        out.println("</body></html>");
    }

    /**
     * The only http call used by the client side is POST in order to deal with unlimited (??)
     * size of stream.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    private final static Gson gson = new GsonBuilder().registerTypeAdapter(java.util.Date.class,
            new UtilDateSerializer()).setDateFormat(DateFormat.LONG).create();

    /**
     * 根据收到的请求，构造调用对象
     * @param request
     * @return
     */
    protected Invocation getInvocation(HttpServletRequest request) {

        String contentType = request.getContentType();
        Invocation invocation = null;

        //通过远程java客户端进行调用
        if (contentType.indexOf(CONTENT_TYPE_BSF) > -1) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(request.getInputStream());
                invocation = (Invocation) ois.readObject();
                ois.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getLocalizedMessage());
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        } //从javascript中以json形式传递参数进行调用
        else {
            //从请求中取得参数
            String serviceName = request.getParameter(SERVICE);
            String methodName = request.getParameter(METHOD);
            String argStr = request.getParameter(ARGS);
            String typeStr = request.getParameter(TYPES);

            System.out.println(">>>invocation: " + serviceName + ", " + methodName + ", " + argStr + ", " + typeStr);

            //将取得参数还原为JSON对象
            /*
            JSONArray jaArgs = JSONArray.fromObject(argStr);
            Object[] args = new Object[jaArgs.size()];
            for (int i = 0; i < jaArgs.size(); i++) {
                args[i] = jaArgs.get(i);
            }*/
            String[] args = JSONConverter.gson().fromJson(argStr, String[].class);

            //将类型还原为JSON对象
            /*
            JSONArray jaTypes = JSONArray.fromObject(typeStr);
            String[] types = new String[jaTypes.size()];
            for (int i = 0; i < jaTypes.size(); i++) {
                types[i] = jaTypes.get(i).toString();
            }*/
            String[] types = JSONConverter.gson().fromJson(typeStr, String[].class);

            //将types转换为实际参数类型
            Class[] paramTypes = ObjectUtil.getActParamTypes(types);

            //将参数args转换为实际参数
            Object[] actArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                actArgs[i] = ObjectUtil.getActParamValue(args[i], paramTypes[i]);
            }

            //构造一个invocation
            ServiceDefinition serviceDef = new ServiceDefinition(serviceName, ServiceFactory.getServiceInterface(serviceName));
            invocation = new Invocation(serviceDef, methodName, paramTypes, actArgs);
        }
        return invocation;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        Invocation invocation = getInvocation(request);

        String contentType = request.getContentType();
        Object result = null;

        //Verifiying the session existence
        HttpSession session = request.getSession(false);
        if (session != null && "invalidateSession".equals(invocation.getMethodName())) {
            session.invalidate();
            try {
                writeHttpServiceResponse(contentType, response, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }

        if (session == null) {
            //this is the first call, we create the session and init it.
            session = request.getSession(true);
            session.setAttribute(STATEFULL_CACHE, new Hashtable());

            //下列关于pricipal的代码为新增加的(lgh 2004-09-30 17:13)
            Principal principal = request.getUserPrincipal();
            String username = null;
            if (principal != null) {
                username = principal.getName();
            }
            if (username == null) {
                username = "";
            }
            log.info("Creation of a new session for : " + request.getContextPath() + " " + username);
        }
        session.setAttribute("ip", request.getRemoteHost());
        
        //Set thread local session for RhSessionContext
        SessionContextImpl.session.set(session);

        try {
            //Is the user trying to authenticate?
            if ("authenticate".equals(invocation.getMethodName())) {
                try {
                    result = authenticate(invocation, request, response);
                    writeHttpServiceResponse(contentType, response, result);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return;
            }

            //we retrieve the login user from the http session if the client used authentication
            User user = (User) session.getAttribute(User.USER_KEY);

            if (user != null) {
                //We use authentication => we set the principal
                setPrincipalForThread(user.getUsername(), user.getPassword());
                //We can make the remote call
                result = invoke(invocation);
            } else {
                result = new RemoteException("Before executing remote call, You must login to the system.");
            }
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
            //The server may have restart => we clear the cache and the IC and retry
            initEjbContext(namingContextProperties);
            serviceCache = new Hashtable();
            try {
                result = invoke(invocation);
            } catch (NoSuchObjectException ex) {
                //There is definitely a problem
                result = new RemoteException("Impossible to make the remote call. Restart your application.");
            }
        }

        //Write the result in the http stream
        response.addHeader("jsessionid", session.getId());
        
        try {
            writeHttpServiceResponse(contentType, response, result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    /**
     * Performs the authentication. If it succeeds, had the login and the password in
     * the web session.
     */
    private Object authenticate(Invocation invocation, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        Object[] args = invocation.getArgs();
        if (args.length != 2) {
            throw new RuntimeException("The authenticate call must have two arguments");
        }

        String user = (String) args[0];
        String password = (String) args[1];

        //We can make the remote call
        Object result = invoke(invocation);

        //If the authentification failed we finish the execution
        if (result instanceof LoginContext) {
            LoginContext loginContext = (LoginContext) result;
            if (loginContext == null) {
                throw new RuntimeException("Login failed!!");
            }
            
            HttpSession session = request.getSession(false);
            String sessionId = session.getId();
            response.addHeader("jsessionid", sessionId);

            
            session.setAttribute(User.USER_KEY, loginContext.getUser());

            session.setAttribute(RhSessionContext.CLIENT_ID, loginContext.getClient_id());
            session.setAttribute(RhSessionContext.ORG_ID, loginContext.getOrg_id());
            session.setAttribute(RhSessionContext.DUTY_ID, loginContext.getDutyId());

            Map attrs = loginContext.getAttributes();
            Set<Map.Entry> entries = attrs.entrySet();
            for (Map.Entry entry : entries) {
                session.setAttribute((String) entry.getKey(), entry.getValue());
            }

            // 创建login session
            LoginSession loginSession = new LoginSession();
            loginSession.setLoginName(loginContext.getUser().getUsername());
            loginSession.setIp(request.getRemoteHost());
            loginSession.setLoginDate(new Date(session.getCreationTime()));
            loginSession.setSessionId(session.getId());
            loginSession.setLiving(true);

            //Session hibernateSession = RhinoCtx.instance().getHibernate().getSession();
            try {
                
                ServiceBase serviceBase = (ServiceBase) ServiceFactory.getService(ServiceBase.NAME);
                //ServiceBase serviceBase = (ServiceBase) RhinoCtx.getBean("serviceBase");
                //hibernateSession.beginTransaction();
                serviceBase.save(loginSession);
                //hibernateSession.getTransaction().commit();
                session.setAttribute("loginSession", loginSession);

            } catch (Exception ex) {
                ex.printStackTrace();
                //hibernateSession.getTransaction().rollback();
            } finally {
                //hibernateSession.close();
            }
        }
        return result;
    }

    private void setPrincipalForThread(String login, String password) {
        //暂时屏蔽, 以便在tomcat中测试通过
        //principalManager.setThreadPrincipal(login, password);
    }

    /**
     * 调用后，应处理对应stateful service 的会话的提交，会话关闭则在destroy时进行
     * @param invocation
     * @return
     * @throws java.rmi.NoSuchObjectException
     */
    private Object invoke(Invocation invocation) throws NoSuchObjectException {

        ServiceDefinition serviceDef = (ServiceDefinition) invocation.getRemoteService();
        //Class serviceInterface = serviceDef.getServiceInterface();
        String serviceName = serviceDef.getServiceName();
        String methodName = invocation.getMethodName();

        Class[] paramTypes = invocation.getParamTypes();
        Object[] args = invocation.getArgs();
        Object targetService = null;

        Object result = null;

        //处理特殊调用，获取服务id
        if ("serviceFactory".equals(serviceName) && "getServiceId".equals(methodName)) {
            try {
                result = ServiceFactory.getServiceId(serviceDef);
                return new ServiceResponse(result);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        try {
            targetService = ServiceFactory.getService(serviceDef);
            //targetService = RhinoCtx.getBean(serviceDef.getServiceName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (targetService == null) {
            return result;
        }

        //Session session = RhinoCtx.instance().getHibernate().getSession();
        try {

            //session.beginTransaction();
            Method invokedMethod = ObjectUtil.findMethod(targetService.getClass(), methodName, paramTypes);
            result = invokedMethod.invoke(targetService, args);
            //session.getTransaction().commit();

        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            if (ex.getTargetException() != null) {
                ex.getTargetException().printStackTrace();
            }
            if (ex.getTargetException() instanceof NoSuchObjectException) {
                NoSuchObjectException noSuchObjectException = (NoSuchObjectException) ex.getTargetException();
                throw noSuchObjectException;
            }
            //session.getTransaction().rollback();

            //The target exception is the only result sent back on the client
            result = ex.getTargetException();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            log.error(null, ex);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            log.error(null, ex);
        } catch (SecurityException ex) {
            ex.printStackTrace();
            log.error(null, ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //1. 关闭相关的资源
            /*
            JbpmConfiguration jbpm = JbpmConfiguration.getInstance();
            JbpmContext jbpmContext = jbpm.getCurrentJbpmContext();
            if (jbpmContext != null) {
            jbpmContext.close();
            }*/
            //session.close();
        }
        return result;
    }

    /**
     * writes the response in the http stream
     */
    private void writeHttpServiceResponse(String contentType, HttpServletResponse response, Object result) throws
            IOException {
        if (contentType.indexOf(CONTENT_TYPE_BSF) > -1) {
            OutputStream outputStream = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            oos.writeObject(new ServiceResponse(result));
            oos.close();
        } else {
            //JSONObject jsonResult = new JSONObject();
        	JsonObject jsonResult = new JsonObject();
            if (result instanceof Throwable) {
                Throwable ex = (Throwable) result;
                jsonResult.addProperty(ERROR, ex.getMessage());
                StringWriter sw = new StringWriter();
                ex.printStackTrace(new PrintWriter(sw));
                jsonResult.addProperty(STACK_TRACE, sw.toString());
            } /*else if (result != null && ObjectUtil.isCollection(result)) {
                jsonResult.put(VALUE, JSONArray.fromObject(result));
            } else if (result instanceof String && JSONUtils.mayBeJSON((String) result)) {
                jsonResult.put(VALUE, JSONObject.fromObject((String) result));
            } else if (JSONUtils.isNumber(result) || JSONUtils.isBoolean(result) || JSONUtils.isString(result)) {
                jsonResult.put(VALUE, result);
            }*/ 
            else {
                System.out.println(">>>result class: " + result.getClass());
                jsonResult.add(VALUE, JSONConverter.toJsonTree(result));
            }
            response.setCharacterEncoding(RESP_ENCODING);
            response.getWriter().write(JSONConverter.gson().toJson(jsonResult));
        }
    }

    /**
     * 根据Target Exception的种类, 重新抛出适当的异常
     * @param e
     */
    public static void throwInvocationTargetException(InvocationTargetException e) {
    }

    private static synchronized void initEjbContext(String jndiProperties) {

        if (namingContext != null) {
            namingContext = null;

            //Running the GC we realease the weak references used to cache
            // the server references.
            System.gc();
        }
        ///_ejbContext = ICFactoryImpl.createInitialContext(jndiProperties);
    }

    /**
     * 当Redeploy的时候，要对serviceCache中的内容清空，否则在查询GRemoteQueryTable时,
     * 因为要用到Stateful SessionBean(RemoteQueryBean)，如果不清空，会报ClassCastException
     */
    public static void clearStatefulCache() {
        serviceCache.clear();
    }
}
