/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import com.bizcreator.util.Monitorable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bizcreator.util.LRUCachePolicy;

/**
 * Least Recently Used cache policy for EnterpriseContexts.
 *
 * @see AbstractInstanceCache
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @version $Revision: 1.4 $
 */
public class LRUServiceContextCachePolicy extends LRUCachePolicy implements Monitorable {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------
    protected static Log log = LogFactory.getLog(LRUServiceContextCachePolicy.class);
    protected static Timer tasksTimer = new Timer(true);
    

    static {
        log.debug("Cache policy timer started, tasksTimer=" + tasksTimer);
    }
    /** The AbstractInstanceCache that uses this cache policy */
    private AbstractInstanceCache m_cache;
    /** The period of the resizer's runs */
    private long m_resizerPeriod = 400 * 1000;
    /** The period of the overager's runs */
    private long m_overagerPeriod = 300 * 1000;
    /** The age after which a bean is automatically passivated */
    private long m_maxBeanAge = 600 * 1000;
    /**
     * Enlarge cache capacity if there is a cache miss every or less
     * this member's value
     */
    private long m_minPeriod = 1 * 1000;
    /**
     * Shrink cache capacity if there is a cache miss every or more
     * this member's value
     */
    private long m_maxPeriod = 60 * 1000;
    /**
     * The resizer will always try to keep the cache capacity so
     * that the cache is this member's value loaded of cached objects
     */
    private double m_factor = 0.75;
    
    /** The overager timer task */
    private TimerTask m_overager;
    /** The resizer timer task */
    private TimerTask m_resizer;
    /** Useful for log messages */
    private StringBuffer m_buffer = new StringBuffer();

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------
    /**
     * Creates a LRU cache policy object given the instance cache that use
     * this policy object.
     */
    public LRUServiceContextCachePolicy(AbstractInstanceCache eic) {
        if (eic == null) {
            throw new IllegalArgumentException("Instance cache argument cannot be null");
        }
        m_cache = eic;
    }
    // Public --------------------------------------------------------

    // Monitorable implementation ------------------------------------
    public void sample(Object s) {
        if (m_cache == null) {
            return;
        }
        BeanCacheSnapshot snapshot = (BeanCacheSnapshot) s;
        LRUList list = getList();
        synchronized (m_cache.getCacheLock()) {
            snapshot.m_cacheMinCapacity = list.m_minCapacity;
            snapshot.m_cacheMaxCapacity = list.m_maxCapacity;
            snapshot.m_cacheCapacity = list.m_capacity;
            snapshot.m_cacheSize = list.m_count;
        }
    }
    // Z implementation ----------------------------------------------
    public void start() {
        if (m_resizerPeriod > 0) {
            m_resizer = new ResizerTask(m_resizerPeriod);
            long delay = (long) (Math.random() * m_resizerPeriod);
            tasksTimer.schedule(m_resizer, delay, m_resizerPeriod);
        }

        if (m_overagerPeriod > 0) {
            m_overager = new OveragerTask(m_overagerPeriod);
            long delay = (long) (Math.random() * m_overagerPeriod);
            tasksTimer.schedule(m_overager, delay, m_overagerPeriod);
        }
    }

    public void stop() {
        if (m_resizer != null) {
            m_resizer.cancel();
        }
        if (m_overager != null) {
            m_overager.cancel();
        }
        super.stop();
    }

    public void destroy() {
        m_overager = null;
        m_resizer = null;
        m_cache = null;
        super.destroy();
    }
    // Y overrides ---------------------------------------------------
    /**
     * Flush is overriden here because in this policy impl
     * flush might not actually remove all the instances from the cache.
     * Those instances that are in use (associated with a transaction) should not
     * be removed from the cache. So, the iteration is done not until the cache is empty
     * but until we tried to age-out every instance in the cache.
     */
    public void flush() {
        int i = size();
        LRUCacheEntry entry = null;
        while (i-- > 0 && (entry = m_list.m_tail) != null) {
            ageOut(entry);
        }
    }
    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------
    @Override
    protected LRUList createList() {
        return new ContextLRUList();
    }

    @Override
    protected void ageOut(LRUCacheEntry entry) {
        if (m_cache == null) {
            return;
        }
        if (entry == null) {
            throw new IllegalArgumentException("Cannot remove a null cache entry");
        }

        if (log.isTraceEnabled()) {
            m_buffer.setLength(0);
            m_buffer.append("Aging out from cache bean ");
            m_buffer.append(m_cache.getServiceName());
            m_buffer.append("with id = ");
            m_buffer.append(entry.m_key);
            m_buffer.append("; cache size = ");
            m_buffer.append(getList().m_count);
            log.trace(m_buffer.toString());
        }

        // This will schedule the passivation
        m_cache.release((ServiceContext) entry.m_object);
    }

    @Override
    protected void cacheMiss() {
        LRUList list = getList();
        ++list.m_cacheMiss;
    }

    // Private -------------------------------------------------------
    private LRUList getList() {
        return m_list;
    }
    // Inner classes -------------------------------------------------
    /**
     * This TimerTask resizes the cache capacity using the cache miss frequency
     * algorithm, that is the more cache misses we have, the more the cache size
     * is enlarged, and viceversa. <p>
     * Of course, the maximum and minimum capacity are the bounds that this
     * resizer never passes.
     */
    protected class ResizerTask extends TimerTask {

        private String m_message;
        private StringBuffer m_buffer;
        private long resizerPeriod;

        protected ResizerTask(long resizerPeriod) {
            this.resizerPeriod = resizerPeriod;
            m_message = "Resized cache for bean " +
                    m_cache.getServiceName() +
                    ": old capacity = ";
            m_buffer = new StringBuffer();
        }

        public void run() {
            // For now implemented as a Cache Miss Frequency algorithm
            if (m_cache == null) {
                cancel();
                return;
            }

            LRUList list = getList();

            // Sync with the cache, since it is accessed also by another thread
            synchronized (m_cache.getCacheLock()) {
                int period = list.m_cacheMiss == 0 ? Integer.MAX_VALUE : (int) (resizerPeriod / list.m_cacheMiss);
                int cap = list.m_capacity;
                if (period <= m_minPeriod && cap < list.m_maxCapacity) {
                    // Enlarge cache capacity: if period == m_minPeriod then
                    // the capacity is increased of the (1-m_factor)*100 %.
                    double factor = 1.0 + ((double) m_minPeriod / period) * (1.0 - m_factor);
                    int newCap = (int) (cap * factor);
                    list.m_capacity = newCap < list.m_maxCapacity ? newCap : list.m_maxCapacity;
                    log(cap, list.m_capacity);
                } else if (period >= m_maxPeriod &&
                        cap > list.m_minCapacity &&
                        list.m_count < (cap * m_factor)) {
                    // Shrink cache capacity
                    int newCap = (int) (list.m_count / m_factor);
                    list.m_capacity = newCap > list.m_minCapacity ? newCap : list.m_minCapacity;
                    log(cap, list.m_capacity);
                }
                list.m_cacheMiss = 0;
            }
        }

        private void log(int oldCapacity, int newCapacity) {
            if (log.isTraceEnabled()) {
                m_buffer.setLength(0);
                m_buffer.append(m_message);
                m_buffer.append(oldCapacity);
                m_buffer.append(", new capacity = ");
                m_buffer.append(newCapacity);
                log.trace(m_buffer.toString());
            }
        }
    }

    /**
     * This TimerTask passivates cached beans that have not been called for a while.
     */
    protected class OveragerTask extends TimerTask {

        private String m_message;
        private StringBuffer m_buffer;

        protected OveragerTask(long period) {
            m_message = getTaskLogMessage() + " " +
                    m_cache.getServiceName() +
                    " with id = ";
            m_buffer = new StringBuffer();
        }

        public void run() {
            if (m_cache == null) {
                cancel();
                return;
            }

            LRUList list = getList();
            long now = System.currentTimeMillis();
            ArrayList passivateEntries = null;
            synchronized (m_cache.getCacheLock()) {
                for (LRUCacheEntry entry = list.m_tail; entry != null; entry = entry.m_prev) {
                    if (now - entry.m_time >= getMaxAge()) {
                        // Attempt to remove this entry from cache
                        if (passivateEntries == null) {
                            passivateEntries = new ArrayList();
                        }
                        passivateEntries.add(entry);
                    } else {
                        break;
                    }
                }
            }
            // We need to do this outside of cache lock because of deadlock possibilities
            // with EntityInstanceInterceptor and Stateful. This is because tryToPassivate
            // calls lock.synch and other interceptor call lock.synch and after call a cache method that locks
            if (passivateEntries != null) {
                for (int i = 0; i < passivateEntries.size(); i++) {
                    LRUCacheEntry entry = (LRUCacheEntry) passivateEntries.get(i);
                    try {
                        m_cache.tryToPassivate((ServiceContext) entry.m_object);
                    } catch (Throwable t) {
                        log.debug("Ignored error while trying to passivate ctx", t);
                    }
                }
            }
        }

        private void log(Object key, int count) {
            if (log.isTraceEnabled()) {
                m_buffer.setLength(0);
                m_buffer.append(m_message);
                m_buffer.append(key);
                m_buffer.append(" - Cache size = ");
                m_buffer.append(count);
                log.trace(m_buffer.toString());
            }
        }

        protected String getTaskLogMessage() {
            return "Scheduling for passivation overaged bean";
        }

        protected String getJMSTaskType() {
            return "OVERAGER";
        }

        protected long getMaxAge() {
            return m_maxBeanAge;
        }
    }

    /**
     * Subclass that logs list activity events.
     */
    protected class ContextLRUList extends LRUList {

        boolean trace = log.isTraceEnabled();

        protected void entryPromotion(LRUCacheEntry entry) {
            if (trace) {
                log.trace("entryPromotion, entry=" + entry);            // The cache is full, temporarily increase it
            }
            if (m_count == m_capacity && m_capacity >= m_maxCapacity) {
                ++m_capacity;
                log.warn("Cache has reached maximum capacity for service " +
                        m_cache.getServiceName() +
                        " - probably because all instances are in use. " +
                        "Temporarily increasing the size to " + m_capacity);
            }
        }

        protected void entryAdded(LRUCacheEntry entry) {
            if (trace) {
                log.trace("entryAdded, entry=" + entry);
            }
        }

        protected void entryRemoved(LRUCacheEntry entry) {
            if (trace) {
                log.trace("entryRemoved, entry=" + entry);
            }
        }

        protected void capacityChanged(int oldCapacity) {
            if (trace) {
                log.trace("capacityChanged, oldCapacity=" + oldCapacity);
            }
        }
    }
}
