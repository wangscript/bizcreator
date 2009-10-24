package com.bizcreator.core.remoting;

import java.lang.reflect.Method;

import javax.management.MBeanServer;
import javax.naming.Context;
import javax.naming.InitialContext;
/*
import org.jboss.remoting.InvocationRequest;
import org.jboss.remoting.ServerInvocationHandler;
import org.jboss.remoting.ServerInvoker;
import org.jboss.remoting.callback.InvokerCallbackHandler;
*/

public class ServiceInvocationHandler //implements ServerInvocationHandler
{
	
	//	The Initial context for EJB lookup
    private static Context namingContext;
    
	/**
     * called to handle a specific invocation
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    /*
    public Object invoke(InvocationRequest request) 
    {
       // Print out the invocation request
       System.out.println("Invocation request is: " + request.getParameter());

       Object param = request.getParameter();
       if (param instanceof Invocation) {
    	   Invocation invocation = (Invocation) param;
    	   Object serviceDef = invocation.getRemoteService();
    	   String methodName = invocation.getMethodName();
    	   Class[] paramTypes = invocation.getParamTypes();
    	   Object[] args = invocation.getArgs();
    	   
    	   Object targetService = null;
    	   
    	   if (serviceDef instanceof EJBDefinition) {
    		   EJBDefinition ejbDef = (EJBDefinition) serviceDef;
    		   //调用EJB
    		   try {
	    		   if (namingContext == null) {
	    			   namingContext = new InitialContext();
	    		   }
	    		   targetService = namingContext.lookup((ejbDef.getServiceName()));
	    		   
	    		   System.out.println(">>>Begin call to an ejb: " + methodName);
	    		   Method invokedMethod = targetService.getClass().getMethod(methodName, paramTypes);
	    		   Object result = invokedMethod.invoke(targetService, args);
	    		   return result;
    		   }
    		   catch (Exception ex) {
    			   ex.printStackTrace();
    			   return null;
    		   }
    	   }
    	   else if (serviceDef instanceof SeamDefinition) {
    		   System.out.println(">>>Begin call to an jboss seam component by component name....");
    		   SeamDefinition seamDef = (SeamDefinition)serviceDef;
    		   String componentName = seamDef.getServiceName();
    		   
    		   //Find the component we're calling
    		   Component component = Component.forName(componentName);
    		   if (component == null)
    			   throw new RuntimeException("No such component: " + componentName);
    		   
    		   //Create an instance of the component
    		    targetService = Component.getInstance(componentName, true);

    		    Class type = null;

    		    if (component.getType().isSessionBean() &&
    		        component.getBusinessInterfaces().size() > 0) {
    		    	for (Class c : component.getBusinessInterfaces()) {
    		        	if (c.isAnnotationPresent(EJB.LOCAL)) {
							type = component.getBusinessInterfaces().iterator().next();
							break;
						}
					}

		    		if (type == null)
		    			throw new RuntimeException(String.format("Type cannot be determined for component [%s]. Please ensure that it has a local interface.", component));
    		    }
    		    
    		    if (type == null)
    		        type = component.getBeanClass();
    		   
    		    try {
					Method invokedMethod = type.getMethod(methodName, paramTypes);
					Object result = invokedMethod.invoke(targetService, args);
					return result;
				} catch (Exception e) {
					e.printStackTrace();
				}
    		    
    	   }
    	   else if (serviceDef instanceof Class) {
    		   //调用Java Bean
    		   System.out.println(">>>Begin call to an java bean by class....");
    	   }
    	   else if (serviceDef instanceof String) {
    		   //根据名称调用Jboss Seam Component
    		   
    	   }
    	   else {
    		
    	   }
       }
       else {
    	   
       }
       
       return "Hello world!";
    }
*/
    /**
     * Adds a callback handler that will listen for callbacks from
     * the server invoker handler.
     *
     * @param callbackHandler
     */
    /*
    public void addListener(InvokerCallbackHandler callbackHandler)
    {
       // NO OP as do not handling callback listeners in this example
    }*/

    /**
     * Removes the callback handler that was listening for callbacks
     * from the server invoker handler.
     *
     * @param callbackHandler
     *//*
    public void removeListener(InvokerCallbackHandler callbackHandler)
    {
       // NO OP as do not handling callback listeners in this example
    }*/

    /**
     * set the mbean server that the handler can reference
     *
     * @param server
     */
    public void setMBeanServer(MBeanServer server)
    {
       // NO OP as do not need reference to MBeanServer for this handler
    }

    /**
     * set the invoker that owns this handler
     *
     * @param invoker
     *//*
    public void setInvoker(ServerInvoker invoker)
    {
       // NO OP as do not need reference back to the server invoker
    }*/
    
}
