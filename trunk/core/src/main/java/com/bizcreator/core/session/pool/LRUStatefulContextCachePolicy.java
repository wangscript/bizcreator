/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import java.util.TimerTask;

/**
 * Least Recently Used cache policy for StatefulSessionEnterpriseContexts.
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.2 $
 */
public class LRUStatefulContextCachePolicy extends LRUServiceContextCachePolicy {
    // Constants -----------------------------------------------------

    // Attributes ----------------------------------------------------
   /* The age after which a bean is automatically removed */
    private long m_maxBeanLife = 1800 * 1000;
    /* The remover timer task */
    private TimerTask m_remover;
    /* The period of the remover's runs */
    private long m_removerPeriod = 800 * 1000;
    /**
     * The typed stateful cache
     */
    private StatefulSessionInstanceCache ssiCache;

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------
    /**
     * Creates a LRU cache policy object given the instance cache that use this
     * policy object.
     */
    public LRUStatefulContextCachePolicy(AbstractInstanceCache eic) {
        super(eic);
        ssiCache = (StatefulSessionInstanceCache) eic;
    }

    // Public --------------------------------------------------------

    // Monitorable implementation ------------------------------------

    // Z implementation ----------------------------------------------
    public void start() {
        super.start();
        if (m_maxBeanLife > 0) {
            m_remover = new RemoverTask(m_removerPeriod);
            long delay = (long) (Math.random() * m_removerPeriod);
            tasksTimer.schedule(m_remover, delay, m_removerPeriod);
        }
    }

    public void stop() {
        if (m_remover != null) {
            m_remover.cancel();
        }
        super.stop();
    }
    // Y overrides ---------------------------------------------------

    // Package protected ---------------------------------------------

    // Protected -----------------------------------------------------

    // Private -------------------------------------------------------

    // Inner classes -------------------------------------------------
    /**
     * This TimerTask removes beans that have not been called for a while.
     */
    protected class RemoverTask extends OveragerTask {

        protected RemoverTask(long period) {
            super(period);
        }

        protected String getTaskLogMessage() {
            return "Removing from cache bean";
        }

        protected void kickOut(LRUCacheEntry entry) {
            remove(entry.m_key);
        }

        protected long getMaxAge() {
            return m_maxBeanLife;
        }

        public void run() {
            if (ssiCache == null) {
                cancel();
                return;
            }

            synchronized (ssiCache.getCacheLock()) {
                log.debug("Running RemoverTask");
                // Remove beans from cache and passivate them
                super.run();
                log.debug("RemoverTask, PassivatedCount=" + ssiCache.getPassivatedCount());
            }
            try {
                // Throw away any passivated beans that have expired
                ssiCache.removePassivated(getMaxAge() - super.getMaxAge());
                log.debug("RemoverTask, done");
            } catch (Throwable t) {
                log.debug("Ignored error trying to remove passivated beans from cache", t);
            }
        }
    }
}

