package com.bizcreator.core;

import com.bizcreator.core.annotation.ServiceInfo;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.session.pool.ServiceContext;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import com.bizcreator.core.remoting.EJB3Definition;
import com.bizcreator.core.remoting.SeamDefinition;
import com.bizcreator.core.remoting.ServiceDefinition;
import com.bizcreator.core.session.ServiceBase;
import com.bizcreator.core.session.pool.InstanceCache;
import com.bizcreator.core.session.pool.InstancePool;
import com.bizcreator.core.session.pool.SessionPoolManager;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 根据服务定义，获取对应的服务，具备下列特征：
 * 1. 支持有状态和无状态两种会话服务；
 * 2. 支持服务对象池；
 * 3. 获取的结果应该是服务的代理
 * 获取无状态的服务，调用完毕后应释放: pool.free(ctx)
 * 在创建新的代理时，应同步
 */
public class ServiceFactory {

    private static Log log = LogFactory.getLog(ServiceFactory.class);
    public final static String NAME = "serviceFactory";
    private static Map serviceCache = new Hashtable();
    private static Context context;
    public static String EAR_NAME = "rhinofield-portal/";
    private static Map<String, Class> serviceInterfaces = new HashMap<String, Class>();
    private static Map<String, Class> serviceClasses = new HashMap<String, Class>();

    public ServiceFactory(Map<String, String> classNames) {
        Set<Map.Entry<String, String>> entries = classNames.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            try {
                Class clazz = Class.forName(entry.getValue());
                serviceClasses.put(entry.getKey(), clazz);
                Class serviceInterface = ServiceUtil.getServiceInterface(clazz);
                if (serviceInterface != null) {
                    log.info(">>>" + entry.getKey() + ", " + serviceInterface);
                    serviceInterfaces.put(entry.getKey(), serviceInterface);
                }
            } catch (ClassNotFoundException ex) {
                log.warn(">>>class of " + entry.getKey() + ": " + entry.getValue() + " not found!");
            }
        }
    }

    public static Class getServiceInterface(String serviceName) {
        return serviceInterfaces.get(serviceName);
    }

    public static Class getServiceClass(String serviceName) {
        return serviceClasses.get(serviceName);
    }

    public static Object getServiceId(ServiceDefinition serviceDef) throws Exception {
        Object result = null;
        ServiceContext ctx = createStatefulService(serviceDef);
        if (ctx != null) {
            result = ctx.getId();
        }
        return result;
    }

    public static Object getService(String serviceName) throws Exception {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            Object result = BizContext.getBean(serviceName);
            if (result == null) {
                Class serviceInterface = serviceInterfaces.get(serviceName);
                if (serviceInterface == null) {
                    serviceInterface = serviceClasses.get(serviceName);
                }
                if (serviceInterface != null) {
                    ServiceDefinition serviceDef = new ServiceDefinition(serviceName, serviceInterface);
                    result = getService(serviceDef);
                }
            }
            return result;
        }
        else {
            return SysContext.pico().getComponent(serviceName);
        }
    }

    public static Object getService(ServiceDefinition serviceDef) throws Exception {

        Object result = serviceCache.get(serviceDef.getServiceName());
        if (result == null) {
            result = BizContext.getBean(serviceDef.getServiceName());
        }
        if (result == null) {
            // Create Service Proxy
            Class serviceInterface = serviceDef.getServiceInterface();
            ServiceInvocationHandler invocationHandler = new ServiceInvocationHandler(serviceDef);
            result = Proxy.newProxyInstance(
                    // Class loader pointing to the right classes from deployment
                    serviceInterface.getClassLoader(),
                    // The classes we want to implement home and handle
                    new Class[]{serviceInterface},
                    // The home proxy as invocation handler
                    invocationHandler);

            serviceCache.put(serviceDef.getServiceName(), result);
        }
        return result;
    }

    public static ServiceContext createStatefulService(ServiceDefinition serviceDef) throws Exception {
        boolean debug = log.isDebugEnabled();

        SessionPoolManager poolManager = SessionPoolManager.get(serviceDef.getServiceName(), serviceDef.getServiceInterface());
        InstanceCache cache = poolManager.getInstanceCache();
        InstancePool pool = poolManager.getInstancePool();

        ServiceContext ctx = null;
        //如果id为空, 需要创建新的实例

        //该方法会获取或创建一个新的ServiceContext
        ctx = pool.get();
        Object id = poolManager.getPersistenceManager().createId(ctx);
        if (debug) {
            log.debug("Created new session ID: " + id);
        }
        ctx.setId(id);

        // call back to the PM to let it know that ejbCreate has been called with success
        poolManager.getPersistenceManager().createdSession(ctx);
        // Insert in cache
        cache.insert(ctx);

        serviceDef.setId(id);

        return ctx;
    }

    public Object createServiceInstance(String serviceName) {
        Object result = null;
        Class clazz = serviceClasses.get(serviceName);
        if (clazz != null) {
            try {
                result = clazz.newInstance();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static ServiceBase getEntityService(Class entityClass) throws Exception {
        for (Class c = entityClass; c != AtomicEntity.class; c = c.getSuperclass()) {
            if (c.isAnnotationPresent(ServiceInfo.class)) {
                ServiceInfo si = (ServiceInfo) c.getAnnotation(ServiceInfo.class);
                String serviceName = si.name();
                ServiceBase service = (ServiceBase) getService(serviceName);
                return service;
            }
        }
        return (ServiceBase) getService(ServiceBase.NAME);
    }

    //--------------------------------------------------------------------------------
    //----------------------- old implements ---------------------
    //--------------------------------------------------------------------------------
    public static Context getContext() throws NamingException {
        if (context == null) {
            context = new InitialContext();
        }
        return context;
    }

    public static Object getEJB3Service(EJB3Definition ejbDef) throws NamingException {
        Object service = serviceCache.get(ejbDef.getServiceName());
        if (service == null) {
            service = getContext().lookup(EAR_NAME + ejbDef.getServiceName());
            serviceCache.put(ejbDef.getServiceName(), service);
        }
        return service;
    }

    public static Object getSeamService(SeamDefinition seamDef) {
        Object service = serviceCache.get(seamDef.getServiceName());
        /*
        if (service == null) {
        //Find the component we're calling
        Component component = Component.forName(seamDef.getServiceName());
        if (component == null)
        throw new RuntimeException("No such component: " + seamDef.getServiceName());

        //Create an instance of the component
        service = Component.getInstance(seamDef.getServiceName(), true);
        serviceCache.put(seamDef.getServiceName(), service);
        }
         */
        return service;
    }

    public static Object getBeanService(String className) {
        return null;
    }

    public static Object getBeanService(Class clazz) {
        return null;
    }

    /*public static Object getSerivice(Object serviceDef) throws NamingException {
        if (serviceDef instanceof EJB3Definition) {
            return getEJB3Service((EJB3Definition) serviceDef);
        } else if (serviceDef instanceof SeamDefinition) {
            return getSeamService((SeamDefinition) serviceDef);
        } else if (serviceDef instanceof String) {
            return getBeanService((String) serviceDef);
        } else if (serviceDef instanceof Class) {
            return getBeanService((Class) serviceDef);
        } else {
            return null;
        }
    }*/
}
