/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;
import EDU.oswego.cs.dl.util.concurrent.FIFOSemaphore;
import com.bizcreator.core.BaseException;
import com.bizcreator.core.BizContext;
import com.bizcreator.core.ServiceFactory;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author lgh
 */
public abstract class AbstractInstancePool implements InstancePool {

    Log log = LogFactory.getLog(AbstractInstancePool.class);    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------
    /** A FIFO semaphore that is set when the strict max size behavior is in effect.
    When set, only maxSize instances may be active and any attempt to get an
    instance will block until an instance is freed.
     */
    private FIFOSemaphore strictMaxSize;
    /** The time in milliseconds to wait for the strictMaxSize semaphore.
     */
    private long strictTimeout = Long.MAX_VALUE;
    /** The pool data structure */
    protected LinkedList pool = new LinkedList();
    /** The maximum number of instances allowed in the pool */
    protected int maxSize = 100;
    /** determine if we reuse EnterpriseContext objects i.e. if we actually do pooling */
    protected boolean reclaim = false;    //服务名称
    
    protected String serviceName;    //服务接口
    protected Class serviceClass;
    
    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    // Public --------------------------------------------------------
    /**
     * @jmx:managed-attribute
     * @return the current pool size
     */
    public int getCurrentSize() {
        synchronized (pool) {
            return this.pool.size();
        }
    }

    /**
     * @jmx:managed-attribute
     * @return the current pool size
     */
    public int getMaxSize() {
        return this.maxSize;
    }

    /** Get the current avaiable count from the strict max view. If there is
     * no strict max then this will be Long.MAX_VALUE to indicate there is no
     * restriction.
     * @jmx:managed-attribute
     * @return the current avaiable count from the strict max view
     */
    public long getAvailableCount() {
        long size = Long.MAX_VALUE;
        if (strictMaxSize != null) {
            size = strictMaxSize.permits();
        }
        return size;
    }

    public void clear() {
        synchronized (pool) {
            freeAll();
        }
    }

    /**
     *   Get an instance without identity.
     *   Can be used by finders,create-methods, and activation
     *
     * @return     Context /w instance
     * @exception   RemoteException
     */
    public ServiceContext get()
            throws Exception {
        boolean trace = log.isTraceEnabled();
        if (trace) {
            log.trace("Get instance " + this + "#" + pool.size() + "#" + serviceClass);
        }
        if (strictMaxSize != null) {
            // Block until an instance is available
            boolean acquired = strictMaxSize.attempt(strictTimeout);
            if (trace) {
                log.trace("Acquired(" + acquired + ") strictMaxSize semaphore, remaining=" + strictMaxSize.permits());
            }
            if (acquired == false) {
                throw new Exception("Failed to acquire the pool semaphore, strictTimeout=" + strictTimeout);
            }
        }

        synchronized (pool) {
            if (pool.isEmpty() == false) {
                return (ServiceContext) pool.removeFirst();
            }
        }

        // Pool is empty, create an instance
        try {
            //Object instance = container.createBeanClassInstance();
            ServiceFactory serviceFactory = (ServiceFactory) BizContext.getBean(ServiceFactory.NAME);
            Object instance = serviceFactory.createServiceInstance(serviceName);
            return create(instance);
        } catch (Throwable e) {
            // Release the strict max size mutex if it exists
            if (strictMaxSize != null) {
                strictMaxSize.release();
            }
            // Don't wrap CreateExceptions
         /*
            if( e instanceof CreateException )
            throw (CreateException) e;
             */
            // Wrap e in an Exception if needed
            Exception ex = null;
            if (e instanceof Exception) {
                ex = (Exception) e;
            } else {
                ex = new UndeclaredThrowableException(e);
            }
            throw new BaseException("Could not instantiate bean", ex);
        }
    }

    /**
     *   Return an instance after invocation.
     *
     *   Called in 2 cases:
     *   a) Done with finder method
     *   b) Just removed
     *
     * @param   ctx
     */
    public void free(ServiceContext ctx) {
        if (log.isTraceEnabled()) {
            String msg = pool.size() + "/" + maxSize + " Free instance:" + this + "#" + ctx.getId() //+"#"+ctx.getTransaction()
                    + "#" + reclaim + "#" + serviceClass;
            log.trace(msg);
        }

        ctx.clear();

        try {
            // If the pool is not full, add the unused context back into the pool,
            // otherwise, just discard the extraneous context and leave it for GC
            boolean addedToPool = false;

            synchronized (pool) {
                if (pool.size() < maxSize) {
                    pool.addFirst(ctx);
                    addedToPool = true;
                }
            }

            if (addedToPool) {
                // If we block when maxSize instances are in use, invoke release on strictMaxSize
                if (strictMaxSize != null) {
                    strictMaxSize.release();
                }
            } else {
                // Get rid of the extraneous instance; strictMaxSize should be null
                // (otherwise we wouldn't have gotten the extra instance)
                discard(ctx);
            }
        } catch (Exception ignored) {
        }
    }

    public void discard(ServiceContext ctx) {
        if (log.isTraceEnabled()) {
            String msg = "Discard instance:" + this + "#" + ctx //+"#"+ctx.getTransaction()
                    + "#" + reclaim + "#" + serviceClass;
            log.trace(msg);
        }

        // If we block when maxSize instances are in use, invoke release on strictMaxSize
        if (strictMaxSize != null) {
            strictMaxSize.release();        // Throw away, unsetContext()
        }
        try {
            ctx.discard();
        } catch (Exception e) {
            if (log.isTraceEnabled()) {
                log.trace("Ctx.discard error", e);
            }
        }
    }
    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------
    protected ServiceContext create(Object instance) throws Exception {
        return new ServiceContext(instance);
    }
    
    // Private -------------------------------------------------------
    /**
     * At undeployment we want to free completely the pool.
     */
    private void freeAll() {
        LinkedList clone = (LinkedList) pool.clone();
        for (int i = 0; i < clone.size(); i++) {
            ServiceContext ec = (ServiceContext) clone.get(i);
            // Clear TX so that still TX entity pools get killed as well
            ec.clear();
            discard(ec);
        }
        pool.clear();
    }    // Inner classes -------------------------------------------------
}
