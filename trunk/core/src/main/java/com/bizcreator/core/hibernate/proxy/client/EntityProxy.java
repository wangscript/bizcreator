/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.hibernate.proxy.client;


import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.SysContext;
import com.bizcreator.core.hibernate.proxy.LazyEntityReference;
import com.bizcreator.core.session.LazyLoading;
import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.NoOp;

/**
 *
 * @author Administrator
 */
public class EntityProxy {//

    private static final Class[] CALLBACK_TYPES = new Class[]{InvocationHandler.class, NoOp.class};

    public interface WriteReplace {

        Object writeReplace() throws ObjectStreamException;
    }

    private static final class Interceptor implements InvocationHandler {

        private Object entity;
        private LazyEntityReference ref;

        Interceptor(LazyEntityReference ref) {
            this.ref = ref;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            
            if (args.length == 0 &&
                    method.getDeclaringClass() == WriteReplace.class &&
                    method.getName().equals("writeReplace")) {
                if (entity != null) {
                    return entity;
                } else {
                    return ref;
                }
            }

            if (entity == null) {
                String endPoint = (String) System.getProperties().get("end_point");
                if ("server".equals(endPoint)) {
                    //将该EntityProxy传到服务器端时, 应直接调用LazyLoadingSession.initializeEntity, 
                    //如: 在客户端修该MenuModel后, 传到服务器更新, 此时subsystem字段可能还是一个EntityProxy
                    //在使用时需要进行初始化
                    //LazyLoading lazyLoading = (LazyLoading) BeanFactory.get().getBean(LazyLoading.NAME);
                    LazyLoading lazyLoading = (LazyLoading) ServiceFactory.getService(LazyLoading.NAME);
                    entity = lazyLoading.initializeEntity(ref.getClassName(), ref.getId(), ref.getToken());
                } else {
                    //entity = ClientSession.getLoader().initializeEntity(ref.getClassName(),ref.getId(),ref.getToken());
                    LazyLoading lazyLoading = (LazyLoading) SysContext.pico().getComponent(LazyLoading.NAME);
                    entity = lazyLoading.initializeEntity(ref.getClassName(), ref.getId(), ref.getToken());
                }
            }

            try {
                return method.invoke(entity, args);
            } catch (InvocationTargetException ite) {
                throw ite;
            }
        }
    }

    public static Object create(final LazyEntityReference ref) throws ClassNotFoundException {

        Class cls = Class.forName(ref.getClassName(), false, Thread.currentThread().getContextClassLoader());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallbackTypes(CALLBACK_TYPES);
        enhancer.setInterfaces(new Class[]{WriteReplace.class});
        enhancer.setCallbackFilter(new CallbackFilter() {

            public int accept(Method m) {
                return Modifier.isPublic(m.getModifiers()) ? 0 : 1;
            }
        });
        enhancer.setCallbacks(new Callback[]{new Interceptor(ref), NoOp.INSTANCE});
        /*
        enhancer.setNamingPolicy(
        new NamingPolicy(){
        public String getClassName(String arg0, String arg1, Object arg2, Predicate arg3) {
        return ref.getClassName() + "$ClientProxy";
        }
        }        
        );
         */
        return enhancer.create();
    }
}
