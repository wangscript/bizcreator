/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.ServiceUtil;
import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.remoting.ServiceDefinition;
import com.bizcreator.core.session.StatefulService;
import com.bizcreator.core.session.pool.InstanceCache;
import com.bizcreator.core.session.pool.InstancePool;
import com.bizcreator.core.session.pool.ServiceContext;
import com.bizcreator.core.session.pool.SessionPoolManager;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务调用句柄，用于生成服务的代理
 * @author lgh
 */
public class ServiceInvocationHandler implements InvocationHandler {

    private ServiceDefinition serviceDef;

    public ServiceInvocationHandler(ServiceDefinition serviceDef) {
        this.serviceDef = serviceDef;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class serviceInterface = serviceDef.getServiceInterface();
        if (ServiceUtil.isStateful(serviceInterface)) {
            return invokeStateful(proxy, method, args);
        }
        else {
            return invokeStateless(proxy, method, args);
        }
    }

    public Object invokeStateless(Object proxy, Method method, Object[] args) throws Throwable {

        //1. 根据ServiceDef得到调用信息
        Object targetService = null;

        //2. 查找对应服务并调用其方法
        SessionPoolManager poolManager = SessionPoolManager.get(serviceDef.getServiceName(), serviceDef.getServiceInterface());
        InstancePool pool = poolManager.getInstancePool();
        ServiceContext ctx = null;

        try {
            ctx = pool.get();
            if (ctx != null) {
                targetService = ctx.getInstance();
                System.out.println(">>>target stateless service: " + targetService);
            }
        }
        catch (Exception ex) {
            throw ex;
        }

        Object result = null;
        try {
            if (targetService == null) return null;
            result = method.invoke(targetService, args);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            pool.free(ctx);
        }

        return result;
    }

    private static Method create;
    private static Method activate;
    private static Method passivate;
    private static Method destroy;

    static {
        try {
            Class[] noArg = new Class[0];
            create = StatefulService.class.getMethod("create", noArg);
            activate = StatefulService.class.getMethod("activate", noArg);
            passivate = StatefulService.class.getMethod("passivate", noArg);
            destroy = StatefulService.class.getMethod("destroy", noArg);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Object invokeStateful(Object proxy, Method method, Object[] args) throws Throwable {

        //boolean debug = log.isDebugEnabled();

        SessionPoolManager poolManager = SessionPoolManager.get(serviceDef.getServiceName(), serviceDef.getServiceInterface());
        InstanceCache cache = poolManager.getInstanceCache();
        InstancePool pool = poolManager.getInstancePool();

        //使用BeanLockManager来同步

        ServiceContext ctx = null;
        //如果id为空, 需要创建新的实例
        if (serviceDef.getId() == null) {
            ctx = ServiceFactory.createStatefulService(serviceDef);
        }
        else {
            //从缓存中获取实例
            ctx = cache.get(serviceDef.getId());
        }

        Object id = ctx.getId();
        if (method.equals(destroy)) {
            System.out.println(">>>destroy service...");
            poolManager.getPersistenceManager().removeSession(ctx);
            ctx.setId(null);
            // Remove from cache
             cache.remove(id);
             pool.free(ctx);
             return null;
        }

        StatefulService targetService = (StatefulService)ctx.getInstance();
        //System.out.println(">>>target stateful service: " + targetService);
        Object result = null;
        try {
            if (targetService == null) return null;
            result = method.invoke(targetService, args);
        }
        catch (Exception ex) {
            throw ex;
        }
        finally {
            //
        }
        return result;
    }

}
