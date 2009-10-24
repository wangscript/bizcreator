package com.bizcreator.core.remoting;

import com.bizcreator.core.ServiceUtil;
import com.bizcreator.core.SysContext;
import com.bizcreator.core.annotation.ServiceInfo;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.session.ServiceBase;
import java.lang.reflect.Proxy;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.picocontainer.Characteristics;

public class ClientServiceFactory {
	
	private static Map serviceCache = new Hashtable();
	private static Map serviceInterfaces = new Hashtable();
    
	public final static boolean useBsf = true;
	
    public ClientServiceFactory(Map<String, Class> services) {
        init(services);
    }
    
    public static void init(Map<String, Class> services) {
        Set <Map.Entry<String, Class>> entries = services.entrySet();
        for (Map.Entry<String, Class> entry : entries) {
            Class clazz = entry.getValue();
            
            serviceInterfaces.put(entry.getKey(), clazz);
            
            Object service = null;
            if (clazz.isInterface()) {
                //如果是接口, 则创建服务代理
                if (!ServiceUtil.isStateful(clazz)) {
                    service = getService(entry.getKey(), entry.getValue());
                }
            }
            else {
                //否则实例化具体的代理类
                try {
                    service  = clazz.newInstance();
                }
                catch (java.lang.InstantiationException ex) {
                    ex.printStackTrace();
                }
                catch (java.lang.IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            if (service != null)
                SysContext.pico().as(Characteristics.CACHE).addComponent(entry.getKey(), service);
        }
    }
    
    public static ServiceBase getEntityService(Class entityClass) {
        /*
        Map<Class, String> map = (Map<Class, String>) SysContext.pico().getComponent("entityServices");
        Object service = null;
        
        //如果entity对应的service没有设置, 则查找其父类的service
        for(Class c = entityClass; c != AtomicEntity.class; c = c.getSuperclass()) {
            String serviceName = map.get(c);
            if (serviceName == null) continue;
            service = getService(serviceName);
            if (service != null) break;
        }
        return (ServiceBase) service;
         */
        for(Class c = entityClass; c != AtomicEntity.class; c = c.getSuperclass()) {
            if (c.isAnnotationPresent(ServiceInfo.class)) {
                ServiceInfo si = (ServiceInfo) c.getAnnotation(ServiceInfo.class);
                String serviceName = si.name();
                ServiceBase service = (ServiceBase) getService(serviceName);
                return service;
            }
        }
        return (ServiceBase) getService("serviceBase");
    }
    
    public static Object getService(String serviceName) {
        Object result = null;
        Class clazz = (Class) serviceInterfaces.get(serviceName);
        if (clazz != null && ServiceUtil.isStateful(clazz)) {
            result = getService(serviceName, clazz);
        }
        else {
            result = SysContext.pico().getComponent(serviceName);
        }
        return result;
    }
    
    public static Object getService(String serviceName, Class serviceInterface) {
        return getService(new ServiceDefinition(serviceName, serviceInterface));
    }
    
    public static Object getService(ServiceDefinition serviceDef) {
        //1. 如果是stateful service，则先获取stateful service 的id
        Class serviceInterface = serviceDef.getServiceInterface();
        Object serviceId = null;
        if (ServiceUtil.isStateful(serviceInterface)) {
            ServiceDefinition sd = new ServiceDefinition("serviceFactory", Object.class);
            Invocation invocation = new Invocation(sd, "getServiceId", new Class[0], new Object[0]);
            try {
                serviceId = HttpSessionClient.getInstance().invokeHttp(invocation);
            }
            catch (Throwable ex) {
                ex.printStackTrace();
            }
            //System.out.println(">>>serviceFactory service id: " + serviceId);
            serviceDef.setId(serviceId);
        }
        
        //2. 生成服务的客户端代理, 对于stateless service可以从cache中取, 
        //   如果是stateful service, 则应生成新的invocationHandler
        ClientInvocationHandler invocationHandler = null;
        Object proxy = null;
        proxy = serviceCache.get(serviceDef);
        if (proxy == null) {
            invocationHandler = new ClientInvocationHandler(serviceDef);
            proxy = Proxy.newProxyInstance(ClientInvocationHandler.class.getClassLoader(),
                new Class[]{serviceDef.getServiceInterface()}, invocationHandler);
            serviceCache.put(serviceDef, proxy);
        }
        else if (serviceId != null){
            invocationHandler = (ClientInvocationHandler) Proxy.getInvocationHandler(proxy);
            invocationHandler.setServiceId(serviceId);
        }
        return proxy;
    }
    
	public static Object getBeanService(String className) {
		ClientInvocationHandler invocationHandler = null;
        Object dynamicProxy = null;

        dynamicProxy = serviceCache.get(className);
        if (dynamicProxy == null) {
        	invocationHandler = new ClientInvocationHandler(className);
        	try {
            dynamicProxy = Proxy.newProxyInstance(ClientInvocationHandler.class.getClassLoader(),
                    new Class[]{Class.forName(className)}, invocationHandler);
        	} catch (ClassNotFoundException ex) {
        		return null;
        	}
           serviceCache.put(className, dynamicProxy);
        }
        return dynamicProxy;
	}
	
	public static Object getBeanService(Class clazz) {
		ClientInvocationHandler invocationHandler = null;
        Object dynamicProxy = null;

        dynamicProxy = serviceCache.get(clazz);
        if (dynamicProxy == null) {
        	invocationHandler = new ClientInvocationHandler(clazz);
            dynamicProxy = Proxy.newProxyInstance(ClientInvocationHandler.class.getClassLoader(),
                    new Class[]{clazz}, invocationHandler);
           serviceCache.put(clazz, dynamicProxy);
        }
        return dynamicProxy;
	}
	
	public static Object getSerivice(Object serviceDef) {
		if (serviceDef instanceof ServiceDefinition) {
            return getService((ServiceDefinition)serviceDef);
		}
		else if (serviceDef instanceof String) {
			return getBeanService((String)serviceDef);
		}
		else if (serviceDef instanceof Class) {
			return getBeanService((Class) serviceDef);
		}
		else return null;
	}
}
