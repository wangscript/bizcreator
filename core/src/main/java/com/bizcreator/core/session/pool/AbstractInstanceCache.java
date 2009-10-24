/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;


import com.bizcreator.util.CachePolicy;
import com.bizcreator.util.Monitorable;
import java.rmi.NoSuchObjectException;
import org.apache.commons.logging.LogFactory;
import java.rmi.RemoteException;
import java.util.Map;
import org.apache.commons.logging.Log;


/**
 *
 * @author lgh
 */
public abstract class AbstractInstanceCache implements InstanceCache, Monitorable {

    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------
    protected static Log log = LogFactory.getLog(AbstractInstanceCache.class);

    /* The object that is delegated to implement the desired caching policy */
    private CachePolicy m_cache;
    /* The mutex object for the cache */
    private final Object m_cacheLock = new Object();
    protected String serviceName;
    protected Class serviceClass;
    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------
    // Monitorable implementation ------------------------------------
    public void sample(Object s) {
        if (m_cache == null) {
            return;
        }
        synchronized (getCacheLock()) {
            BeanCacheSnapshot snapshot = (BeanCacheSnapshot) s;
            snapshot.m_passivatingBeans = 0;
            CachePolicy policy = getCache();
            if (policy instanceof Monitorable) {
                ((Monitorable) policy).sample(s);
            }
        }
    }

    public void setCachePolicy(CachePolicy cachePolicy) {
        this.m_cache = cachePolicy;
    }

    public Map retrieveStatistic() {
        return null;
    }

    public void resetStatistic() {
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    // Public --------------------------------------------------------
    /* From InstanceCache interface */
    public ServiceContext get(Object id)
            throws RemoteException, NoSuchObjectException {
        if (id == null) {
            throw new IllegalArgumentException("Can't get an object with a null key");
        }
        ServiceContext ctx;
        synchronized (getCacheLock()) {
            CachePolicy cache = getCache();
            ctx = (ServiceContext) cache.get(id);
            if (ctx == null) {
                try {
                    ctx = acquireContext();
                    setKey(id, ctx);
                    if (doActivate(ctx) == false) // This is a recursive activation
                    {
                        return ctx;
                    }
                    logActivation(id);
                    // the cache will throw an IllegalStateException if we try to insert
                    // something that is in the cache already, so we don't check here
                    cache.insert(id, ctx);
                } catch (Throwable x) {
                    x.printStackTrace();
                    throw new NoSuchObjectException(x.getMessage());
                }
            }
        }
        return ctx;
    }

    /* From InstanceCache interface */
    public void insert(ServiceContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("Can't insert a null object in the cache");
        }
        Object key = getKey(ctx);
        synchronized (getCacheLock()) {
            // the cache will throw an IllegalStateException if we try to insert
            // something that is in the cache already, so we don't check here
            getCache().insert(key, ctx);
        }
    }

    /**
     * Tries to passivate the instance. If the instance is in use then the instance
     * will be passivated later according to the container's commit option and max age.
     */
    protected void tryToPassivate(ServiceContext ctx) {
        tryToPassivate(ctx, false);
    }

    /**
     * Tries to passivate the instance. If the instance is in use and passivateAfterCommit
     * parameter is true then the instance will passivated after the transaction commits.
     * Otherwise, the instance will be passivated later according to the container's
     * commit option and max age.
     */
    protected void tryToPassivate(ServiceContext ctx, boolean passivateAfterCommit) {
        Object id = ctx.getId();
        if (id == null) {
            return;
        }
    /*
    BeanLock lock = getContainer().getLockManager().getLock(id);
    boolean lockedBean = false;
    try {
    //If this is a BeanLockExt only attempt the lock as the call to
    // remove is going to have to acquire the cache lock, but this may already
    // be held since this method is called by passivation policies without
    // the cache lock. This can lead to a deadlock as in the case of a size based
    // eviction during a cache get attempts to lock the bean that has been
    // locked by an age based background thread as seen in bug 987389 on
    // sourceforge.
    
    if (lock instanceof BeanLockExt) {
    BeanLockExt lock2 = (BeanLockExt) lock;
    lockedBean = lock2.attemptSync();
    if (lockedBean == false) {
    unableToPassivateDueToCtxLock(ctx, passivateAfterCommit);
    return;
    }
    } else {
    // Use the blocking sync
    lock.sync();
    lockedBean = true;
    }
    
    if (canPassivate(ctx)) {
    try {
    remove(id);
    passivate(ctx);
    freeContext(ctx);
    } catch (Exception ignored) {
    log.warn("failed to passivate, id=" + id, ignored);
    }
    } else {
    // Touch the entry to make it MRU
    synchronized (getCacheLock()) {
    getCache().get(id);
    }
    
    unableToPassivateDueToCtxLock(ctx, passivateAfterCommit);
    }
    } finally {
    if (lockedBean) {
    lock.releaseSync();
    }
    
    // getLock is adding a ref count so we need to decrement it.
    getContainer().getLockManager().removeLockRef(id);
    }
     */
    }

    /**
     * Passivates and removes the instance from the cache.
     * If the instance is in use then removal and passivation will be scheduled until
     * after transaction ends
     */
    public void release(ServiceContext ctx) {
        if (ctx == null) {
            throw new IllegalArgumentException("Can't release a null object");        // Here I remove the bean; call to remove(id) is wrong
        // cause will remove also the cache lock that is needed
        // by the passivation, that eventually will remove it.
      /* the removal should only be done if the instance is not in use.
        this is taken care of in tryToPassivate
        Object id = getKey(ctx);
        synchronized (getCacheLock())
        {
        if (getCache().peek(id) != null)
        getCache().remove(id);
        }
         */
        }
        tryToPassivate(ctx, true);
    }

    /** 
     * From InstanceCache interface 
     * @jmx:managed-operation 
     */
    public void remove(Object id) {
        if (id == null) {
            throw new IllegalArgumentException("Can't remove an object using a null key");
        }
        synchronized (getCacheLock()) {
            if (getCache().peek(id) != null) {
                getCache().remove(id);
            }
        }
    }

    public boolean isActive(Object id) {
        // Check whether an object with the given id is available in the cache
        synchronized (getCacheLock()) {
            return getCache().peek(id) != null;
        }
    }

    /** Get the current cache size
     * @jmx:managed-attribute
     * @return the size of the cache
     */
    public long getCacheSize() {
        int cacheSize = m_cache != null ? m_cache.size() : 0;
        return cacheSize;
    }

    /** Flush the cache.
     * @jmx:managed-operation
     */
    public void flush() {
        if (m_cache != null) {
            m_cache.flush();
        }
    }

    /** Get the passivated count.
     * @jmx:managed-attribute
     * @return the number of passivated instances.
     */
    public long getPassivatedCount() {
        return 0;
    }

    /**
     * Display the cache policy.
     * 
     * @jmx:managed-attribute
     * @return the cache policy as a string.
     */
    public String getCachePolicyString() {
        return m_cache.toString();
    }

    /* From Service interface*/
    public void create() throws Exception {
        getCache().create();
    }
    /* From Service interface*/

    public void start() throws Exception {
        getCache().start();
    }
    /* From Service interface*/

    public void stop() {
        // Empty the cache
        synchronized (getCacheLock()) {
            getCache().stop();
        }
    }

    /* From Service interface*/
    public void destroy() {
        synchronized (getCacheLock()) {
            getCache().destroy();
        }
        this.m_cache = null;
    }
    // Y overrides ---------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------
    protected void logActivation(Object id) {
        if (log.isTraceEnabled()) {
            StringBuffer m_buffer = new StringBuffer(100);
            m_buffer.append("Activated bean ");
            m_buffer.append(serviceName);
            m_buffer.append(" with id = ");
            m_buffer.append(id);
            log.trace(m_buffer.toString());
        }
    }

    protected void logPassivation(Object id) {
        if (log.isTraceEnabled()) {
            StringBuffer m_buffer = new StringBuffer(100);
            m_buffer.append("Passivated bean ");
            m_buffer.append(serviceName);
            m_buffer.append(" with id = ");
            m_buffer.append(id);
            log.trace(m_buffer.toString());
        }
    }

    protected void unableToPassivateDueToCtxLock(ServiceContext ctx, boolean passivateAfterCommit) {
        log.warn("Unable to passivate due to ctx lock, id=" + ctx.getId());
    }

    /**
     * Returns the cache policy used for this cache.
     */
    protected CachePolicy getCache() {
        return m_cache;
    }

    /**
     * Returns the mutex used to sync access to the cache policy object
     */
    public Object getCacheLock() {
        return m_cacheLock;
    }

    /**
     * Passivates the given EnterpriseContext
     */
    protected abstract void passivate(ServiceContext ctx) throws RemoteException;

    /**
     * Activates the given EnterpriseContext
     */
    protected abstract void activate(ServiceContext ctx) throws RemoteException;

    /**
     * Activate the given EnterpriseContext
     * 
     * @param ctx the context
     * @return false if we recursively activating
     * @throws RemoteException for any error
     */
    protected boolean doActivate(ServiceContext ctx) throws RemoteException {
        activate(ctx);
        return true;
    }

    /**
     * Acquires an EnterpriseContext from the pool
     */
    protected abstract ServiceContext acquireContext() throws Exception;

    /**
     * Frees the given EnterpriseContext to the pool
     */
    protected abstract void freeContext(ServiceContext ctx);

    /**
     * Returns the key used by the cache to map the given context
     */
    protected abstract Object getKey(ServiceContext ctx);

    /**
     * Sets the given id as key for the given context
     */
    protected abstract void setKey(Object id, ServiceContext ctx);

    /**
     * Returns whether the given context can be passivated or not
     *
     */
    protected abstract boolean canPassivate(ServiceContext ctx);    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
}
