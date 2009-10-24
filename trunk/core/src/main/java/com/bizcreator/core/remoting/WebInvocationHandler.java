package com.bizcreator.core.remoting;

import javax.management.MBeanServer;
/*
import org.jboss.remoting.InvocationRequest;
import org.jboss.remoting.ServerInvocationHandler;
import org.jboss.remoting.ServerInvoker;
import org.jboss.remoting.callback.InvokerCallbackHandler;
 */
//import org.jboss.seam.core.ManagedJbpmContext;

/**
 * @author <a href="mailto:tom.elrod@jboss.com">Tom Elrod</a>
 */
public class WebInvocationHandler //implements ServerInvocationHandler
{
   // String to be returned from invocation handler upon client invocation calls.
   public static final String RESPONSE_VALUE = "This is the return to SampleInvocationHandler invocation";
   public static final ComplexObject OBJECT_RESPONSE_VALUE = new ComplexObject(5, "dub", false);
   public static final ComplexObject LARGE_OBJECT_RESPONSE_VALUE = new ComplexObject(5, "dub", false, 7568);

   public static final String NULL_RETURN_PARAM = "return_null";
   public static final String OBJECT_RETURN_PARAM = "return_object";
   public static final String THROW_EXCEPTION_PARAM = "throw_exception";
   public static final String STRING_RETURN_PARAM = "return_string";
   public static final String USER_AGENT_PARAM = "user_agent";
   public static final String HTML_PAGE_RESPONSE = "<html><head><title>Test HTML page</title></head><body>" +
                                                   "<h1>HTTP/Servlet Test HTML page</h1><p>This is a simple page served for test." +
                                                   "<p>Should show up in browser or via invoker client</body></html>";


   /**
    * called to handle a specific invocation
    *
    * @param invocation
    * @return
    * @throws Throwable
    *//*
   public Object invoke(InvocationRequest invocation) throws Throwable
   {
      // Print out the invocation request
      System.out.println("Invocation request is: " + invocation.getParameter());
      if(NULL_RETURN_PARAM.equals(invocation.getParameter()))
      {
         return null;
      }
      else if(THROW_EXCEPTION_PARAM.equals(invocation.getParameter()))
      {
         throw new Exception("This is an exception being thrown as part of test case.  It is intentional.");
      }
      else if(invocation.getParameter() instanceof ComplexObject)
      {
         ComplexObject obj = (ComplexObject) invocation.getParameter();
         if(obj.getSize() > 1024)
         {
            return LARGE_OBJECT_RESPONSE_VALUE;
         }
         else
         {
            return OBJECT_RESPONSE_VALUE;
         }
      }
      else if(STRING_RETURN_PARAM.equals(invocation.getParameter()))
      {
         // Just going to return static string as this is just simple example code.
    	  return RESPONSE_VALUE;
      }
      else if(USER_AGENT_PARAM.equals(invocation.getParameter()))
      {
         // return user agent found in map
         return invocation.getRequestPayload().get("user-agent");
      }
      else
      {
         return HTML_PAGE_RESPONSE;
      }
   }*/

   /**
    * Adds a callback handler that will listen for callbacks from
    * the server invoker handler.
    *
    * @param callbackHandler
    *//*
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
