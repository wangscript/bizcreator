/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author lgh
 */
public class StatefulSessionInstanceCache extends AbstractInstanceCache {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------

    /* The container */
    //private StatefulSessionContainer m_container;
    /** The map<id, Long> that holds passivated bean ids that have been removed
     * from the cache and passivated to the pm along with the time of passivation
     */
    private ConcurrentReaderHashMap passivatedIDs = new ConcurrentReaderHashMap();

    /* Ids that are currently being activated */
    private HashSet activating = new HashSet();

    /* Used for logging */
    private StringBuffer buffer = new StringBuffer();
    /**
     * This is the persistence manager for this container
     */
    protected StatefulSessionPersistenceManager persistenceManager;
    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------
    public StatefulSessionInstanceCache(String serviceName, Class serviceClass) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }

    public SessionPoolManager getPoolManager() {
        return SessionPoolManager.get(serviceName, serviceClass);
    }
    // Public --------------------------------------------------------
    public StatefulSessionPersistenceManager getPersistenceManager() {
        return getPoolManager().getPersistenceManager();
    }

    /** Get the passivated count.
     * @jmx:managed-attribute
     * @return the number of passivated instances.
     */
    @Override
    public long getPassivatedCount() {
        return passivatedIDs.size();
    }

    public void destroy() {
        synchronized (this) {
            //this.m_container = null;
        }
        passivatedIDs.clear();
        super.destroy();
    }

    protected void passivate(ServiceContext ctx) throws RemoteException {
        getPersistenceManager().passivateSession(ctx);
        passivatedIDs.put(ctx.getId(), new Long(System.currentTimeMillis()));
    }

    protected void activate(ServiceContext ctx) throws RemoteException {
        getPersistenceManager().activateSession(ctx);
        passivatedIDs.remove(ctx.getId());
    }

    @Override
    protected boolean doActivate(ServiceContext ctx) throws RemoteException {
        Object id = ctx.getId();
        synchronized (activating) {
            // This is a recursive invocation
            if (activating.contains(id)) {
                return false;
            }
            activating.add(id);
        }
        try {
            return super.doActivate(ctx);
        } finally {
            synchronized (activating) {
                activating.remove(id);
            }
        }
    }

    protected ServiceContext acquireContext() throws Exception {
        return getPoolManager().getInstancePool().get();
    }

    protected void freeContext(ServiceContext ctx) {
        getPoolManager().getInstancePool().free(ctx);
    }

    protected Object getKey(ServiceContext ctx) {
        return ctx.getId();
    }

    protected void setKey(Object id, ServiceContext ctx) {
        ctx.setId(id);
    }

    protected boolean canPassivate(ServiceContext ctx) {
        /*
        if (ctx.isLocked()) {
        // The context is in the interceptor chain
        return false;
        } else if (m_container.getLockManager().canPassivate(ctx.getId()) == false) {
        return false;
        } else {
        if (ctx.getTransaction() != null) {
        try {
        return (ctx.getTransaction().getStatus() == Status.STATUS_NO_TRANSACTION);
        } catch (SystemException e) {
        // SA FIXME: not sure what to do here
        return false;
        }
        }
        }*/
        return true;
    }

    /** Remove all passivated instances that have been inactive too long.
     * @param maxLifeAfterPassivation the upper bound in milliseconds that an
     * inactive session will be kept.
     */
    protected void removePassivated(long maxLifeAfterPassivation) {
        StatefulSessionPersistenceManager store = getPoolManager().getPersistenceManager();
        long now = System.currentTimeMillis();
        log.debug("removePassivated, now=" + now + ", maxLifeAfterPassivation=" + maxLifeAfterPassivation);
        boolean trace = log.isTraceEnabled();
        Iterator entries = passivatedIDs.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            Object key = entry.getKey();
            Long value = (Long) entry.getValue();
            if (value != null) {
                long passivationTime = value.longValue();
                if (now - passivationTime > maxLifeAfterPassivation) {
                    preRemovalPreparation(key);
                    store.removePassivated(key);
                    //if (trace)
                    //log(key, passivationTime);
                    // Must use iterator to avoid ConcurrentModificationException
                    entries.remove();
                    postRemovalCleanup(key);
                }
            }
        }
    }
    // Protected -----------------------------------------------------
    protected void preRemovalPreparation(Object key) {
        //  no-op...extending classes may add prep
    }

    protected void postRemovalCleanup(Object key) {
        //  no-op...extending classes may add cleanup
    }

    // Private -------------------------------------------------------
    private void log(Object key, long passivationTime) {
        if (log.isTraceEnabled()) {
            buffer.setLength(0);
            buffer.append("Removing from storage bean '");
            buffer.append(serviceName);
            buffer.append("' with id = ");
            buffer.append(key);
            buffer.append(", passivationTime=");
            buffer.append(passivationTime);
            log.trace(buffer.toString());
        }
    }    // Inner classes -------------------------------------------------
}
