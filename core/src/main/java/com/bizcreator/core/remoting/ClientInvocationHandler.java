package com.bizcreator.core.remoting;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
/*
import org.jboss.remoting.Client;
import org.jboss.remoting.InvokerLocator;
import foxtrot.Task;
import foxtrot.Worker;
import javax.swing.SwingWorker;
*/

public class ClientInvocationHandler implements InvocationHandler {
	
    
	//	 Default locator values
	private static String locatorURI = "servlet://192.168.0.188/portal/SeamInvokerServlet";
	private Object serviceDef;
	
	
	public ClientInvocationHandler(Object serviceDef) {
		this.serviceDef = serviceDef;
	}
	
    public void setServiceId(Object serviceId) {
        ((ServiceDefinition) serviceDef).setId(serviceId);
    }
    
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        //BusyHelper.fireBusyEvent(true);
        Object result = null;
        //try {
        /*
        result = (String) Worker.post(new Task() {
            Object obj = null;
            public Object run() throws Exception {
                try {
                    if (ClientServiceFactory.useBsf) {
                        obj = invokeByBsf(proxy, method, args);
                    } else {
                        obj = invokeByJbossRemoting(proxy, method, args);
                    }
                }
                catch (Throwable ex) {
                    ex.printStackTrace();
                }
                return obj;
            }
        });
        
        //} catch (Exception x) { }
       SwingWorker worker = new SwingWorker<Object, Object>() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                if (ClientServiceFactory.useBsf) {
                    return invokeByBsf(proxy, method, args);
                } else {
                     return invokeByJbossRemoting(proxy, method, args);
                }
                }catch (Throwable ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
        };
        
        worker.execute();
        result = worker.get();
        */
        
        
        if (ClientServiceFactory.useBsf) {
            result = invokeByBsf(proxy, method, args);
        } else {
            result = invokeByJbossRemoting(proxy, method, args);
        }
        
        //BusyHelper.fireBusyEvent(false);
        return result;
    }
	
	public Object invokeByJbossRemoting(Object proxy, Method method, Object[] args) throws Throwable {
            /*
		InvokerLocator locator = new InvokerLocator(locatorURI);
		
		Client remotingClient = new Client(locator);
		try {
			remotingClient.connect();
			Invocation invocation = new Invocation(serviceDef, 
					method.getName(), method.getParameterTypes(), args);
			Object response = remotingClient.invoke(invocation);
			return response;
		}
		catch (Throwable throwable) {
			throw new Exception(throwable);
		}
		finally {
			if (remotingClient != null) {
	            remotingClient.disconnect();
	         }
		}*/
            return null;
	}
	
	public Object invokeByBsf(Object proxy, Method method, Object[] args) throws Throwable {
		Invocation invocation = new Invocation(serviceDef, 
				method.getName(), method.getParameterTypes(), args);
		return HttpSessionClient.getInstance().invokeHttp(invocation);
	}
}
