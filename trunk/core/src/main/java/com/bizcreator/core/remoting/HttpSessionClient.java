package com.bizcreator.core.remoting;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;

import com.bizcreator.core.remoting.Invocation;
import com.bizcreator.util.BusyHelper;
import com.bizcreator.util.UtilTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class handle the http protocole for the application server communication.
 * Client classes can make http calls in parallel threads. The number of thread
 * is limited by the field maxThreadCount.
 * The parameters of the communication (port, host, ...)can be set by the ressource
 */

public class HttpSessionClient {
    
    private static Log log = LogFactory.getLog( HttpSessionClient.class);
    
    private static int requestNb;
    private String sessionId;

     /** Default call method is http*/
    private static final String DEFAULT_PROTOCOL = "http";
    
    /** Default Server Host*/
    private static final String SERVER_HOST = "localhost";
    
     /** Default Server Port*/
    private static final int SERVER_PORT = 80;
    
    /** Default Server File*/
    //private static final String SERVER_CONTEXT = "rhinofield";
    private static final String SERVER_CONTEXT = "/wui";
    
    private static final String UNAUTHENTICATED_SERVLET = "/BsfInvokerServlet";
    
    private static final String AUTHENTICATED_SERVLET = "/authenticatedHttpSession";

    /** Default Server File*/
    private static final int DEFAULT_THREAD_COUNT = 16;

    /** BSF default mime type */
    private static final String MIME_TYPE = "application/x-bsf";

    //private static SSLSocketFactory sslFactory = null;
    
    /** Default Server File*/
    private static final String SERVER_FILE = "/portal/BsfInvokerServlet";
    
    /** 
     * The method used to transmit the call, the default is http but it can be https
     * to secure the communication
     */
    private String protocol;
    /** Where is the server */
    private String host;
    
    /** Http Port used */
    private int port = -1;
    
    //The context used at deployment time.
    private String context;
    /** Used for authentification */
    
    /** What we ask on the server */
    private String serverFile = null;

    /** The number of parallel thread used to perform the http call. */
    private int maxThreadCount = DEFAULT_THREAD_COUNT;

    /** The number of thread that are currently making a call */
    private int curUsedThread = 0;

    // Singleton attribute
    protected static HttpSessionClient _instance = null;
    
    private static int invokeCount;
    
    private String login;
    /** password used for basic authentification */
    private String pass;
    
    /**
     * Default constructor
     */
    protected HttpSessionClient() {
        super();
    }
    
    /**
     * Singleton instanciation
     */
    public static HttpSessionClient getInstance() throws IllegalStateException {
        if (_instance == null) {
            _instance = new HttpSessionClient();
        }
        return _instance;
    }
    
    /**
     * Performs the http call.
     */
    public Object invokeHttp(Invocation request) throws Throwable
    {
        //log.info("[" + invokeCount++ + "] Calling method: " + request.getMethodName());
        String file = getServerFile();
        try {
            getThreadLock();

            int currentRequestNb = requestNb++;
            //log.debug("Start remote call " + currentRequestNb + " " + request.getMethodName());
            UtilTimer utilTimer = new UtilTimer();

            ServiceResponse httpResponse;

            //A session exists in the server we use URL rewriting to pass the session id
            if ( sessionId != null ) {
                StringBuffer sb = new StringBuffer();
                sb.append(file.toString());
                sb.append(";jsessionid=");
                sb.append(sessionId);
                file = sb.toString();
            }
            
            //BusyHelper.fireBusyEvent(true);
            
            URL url = new URL(getProtocol(), getHost(), getPort(), file);


            //utilTimer.timerString(">>>start invoking remote method.");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            
            // Set the content type to be application/x-bsf
            httpURLConnection.setRequestProperty("Content-Type", MIME_TYPE);

            //If we have a login and a password => we can perform a basic authentification
/*
            if (isAuthenticatedCall()){
                String loginAndPass = login + ':' + pass;
                httpURLConnection.setRequestProperty("Authorization",
                        "Basic " + new String (Base64.encodeBase64(loginAndPass.getBytes())));
            }
*/
           
            ObjectOutputStream oos = new ObjectOutputStream(httpURLConnection.getOutputStream());
            oos.writeObject(request);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(httpURLConnection.getInputStream());
            
            httpResponse = (ServiceResponse) ois.readObject();
            //System.out.println(">>>http response: " + httpResponse);
            sessionId = httpURLConnection.getHeaderField("jsessionid");
            ois.close();
            
            httpURLConnection.disconnect();

           //utilTimer.timerString(">>>finished invoke remote method.");

            //log.debug("Ending remote call " + currentRequestNb);
            //BusyHelper.fireBusyEvent(false);
            if (httpResponse.isExceptionThrown()) {
                throw httpResponse.getThrowable();
            }

            return httpResponse.getResult();
        }
        catch (Exception ex) {
            System.out.println("ex message: " + ex.getMessage());
            ex.printStackTrace();
            throw new RemoteException(ex.getMessage(), ex);
        }
        /*
        catch (java.io.IOException e) {
            e.printStackTrace();
            if (e instanceof RemoteException){
                throw e;
            }else{
                String message = "Failure during the http remote call on http://"
                        + host + ":" + port + file;
                
                //log.fatal( message, e );
                throw new RemoteException(message, e );
            }
        }
        catch (ClassNotFoundException e) {
            String message = "Failure during the http remote call";
            e.printStackTrace();
            //log.fatal(message, e);
            throw new RemoteException(message, e);
        }
        */
        finally {
            releaseThreadLock();
        }
    }

    /**
     * This method is used to limit the concurrent http call to the max
     * fixed by maxThreadCount and to wait the end of the first call that
     * will return the session id.
     */
    private synchronized void getThreadLock() {
        while (sessionId == null && curUsedThread > 1) {
            try {
                log.info("No session. Only one thread is authorized. Waiting ...");
                wait();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }

        while (curUsedThread >= maxThreadCount) {
            try {
                log.info("Max concurent http call reached. Waiting ...");
                wait();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e.getLocalizedMessage());
            }
        }
        curUsedThread++;
    }

    private synchronized void releaseThreadLock() {
        curUsedThread--;
        notify();
    }

    public String getProtocol() {
        return protocol == null ? DEFAULT_PROTOCOL : protocol;
    }
    /**
     * sets the used protocol. The default one is http but you can set it to
     * https to use a secure communication.
     * @param protocol
     */
    public void setProtocol( String protocol) {
        this.protocol = protocol;
    }
    
    public String getHost() {
        return host == null ? SERVER_HOST : host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public int getPort() {
        return port == -1 ? SERVER_PORT : port;
    }
    
    public void setPort(int port) {
        //System.out.println(">>>port: " + port);
        this.port = port;
    }
    
    private boolean contextChanged = false;
    /**
     * Set the context used at deployment time. For instance if the packaging of
     * the remoting war inside of the ear use the context myApp, you should call
     * setContext("myApp") on the HttpServiceFactory to reach the server.
     *
     * @param context
     */
    public void setContext(String context) {
        while (context.endsWith("/")){
            context = context.substring(0,context.length() -1 );
        }
        this.context = context;
        contextChanged = true;
        log.info( "Server context is set to: " + context);
    }
    
    
    private String getServerFile() {
        if (serverFile != null && !contextChanged) return serverFile;
        
        StringBuffer sb = new StringBuffer();
        context = context == null ? SERVER_CONTEXT : context;
        if (!context.startsWith("/")) {
            sb.append('/');
        }
        sb.append(context);
        if (isAuthenticatedCall()) {
            sb.append(AUTHENTICATED_SERVLET);
        }
        else {
            sb.append(UNAUTHENTICATED_SERVLET);
        }
        serverFile = sb.toString();
        contextChanged = false;
        //System.out.println(">>>invoke server: " + getProtocol() + "://" + getHost() + ":" + getPort() + "/" + serverFile);
        //log.info("server file: " + serverFile);
        return serverFile;
    }
    
    private boolean isAuthenticatedCall() {
        return login != null && pass != null;
    }

    public int getThreadCount() {
        return maxThreadCount;
    }

    public void setThreadCount(int threadCount) {
        this.maxThreadCount = threadCount;
        log.info("Max concurrent thread set to " + threadCount);
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

}

