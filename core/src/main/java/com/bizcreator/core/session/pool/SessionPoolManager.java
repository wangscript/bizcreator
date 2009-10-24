/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.pool;

import com.bizcreator.util.CachePolicy;
import java.util.Hashtable;
import javax.ejb.Stateful;

/**
 * 管理与注册对象相关的特征：
 * 1. InstanceCache
 * 2. InstancePool
 * 3. PersistenceManager
 * @author lgh
 */
public class SessionPoolManager {
    
    
    private String serviceName;
    
    private Class serviceClass;
    
    //<serviceName, manager>
    private static Hashtable<String, SessionPoolManager> managers = new Hashtable<String, SessionPoolManager>();
    
    //对象实例缓存
    //private static Hashtable<String, InstanceCache> instanceCaches = new Hashtable<String, InstanceCache>(); 
    private InstanceCache instanceCache;
    
    //对象池
    //private static Hashtable<String, InstancePool> instancePools = new Hashtable<String, InstancePool>(); 
    private InstancePool instancePool;
    
    
    //持久化管理器
    //private static Hashtable<String, StatefulSessionPersistenceManager> pms = new Hashtable<String, StatefulSessionPersistenceManager>(); 
    private StatefulSessionPersistenceManager persistenceManager;
    
    private SessionPoolManager(String serviceName, Class serviceClass) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }
    
    public synchronized static SessionPoolManager get(String serviceName, Class serviceClass) {
        SessionPoolManager manager = managers.get(serviceName);
        if (manager == null) {
            manager = new SessionPoolManager(serviceName, serviceClass);
            managers.put(serviceName, manager);
        }
        return manager;
    }
    
    public synchronized InstanceCache getInstanceCache() {
        if (instanceCache == null) {
            instanceCache = new StatefulSessionInstanceCache(serviceName, serviceClass);
            CachePolicy policy = new LRUStatefulContextCachePolicy((AbstractInstanceCache)instanceCache);
            try {
                policy.create();
                policy.start();
                instanceCache.setCachePolicy(policy);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return instanceCache;
    }
    
    public synchronized InstancePool getInstancePool() {
        if (instancePool == null) {
            if (serviceClass.isAnnotationPresent(Stateful.class)) {
                instancePool = new StatefulSessionInstancePool(serviceName, serviceClass);
            }
            else {
                instancePool = new StatelessSessionInstancePool(serviceName, serviceClass);
            }
        }
        return instancePool;
    }
    
    public synchronized StatefulSessionPersistenceManager getPersistenceManager() {
        if (persistenceManager == null) {
            persistenceManager = new StatefulSessionFilePersistenceManager(serviceName, serviceClass);
        }
        return persistenceManager;
    }
}
