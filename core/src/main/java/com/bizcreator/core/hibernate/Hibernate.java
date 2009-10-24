/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.hibernate;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Administrator
 */
public class Hibernate {

    
    
    private SessionFactory sessionFactory;
    private final ThreadLocal<Session> sessionLocal  = new ThreadLocal<Session>();
    
    private Set classpathUrls = new HashSet();

    public void create() {
       
       System.out.println("Hibernate Server creating; " + this);
    }

    /**
     * Configure Hibernate and bind the <tt>SessionFactory</tt> to JNDI.
     */
    public void start() throws Exception {
        
        System.out.println("Hibernate Server starting; " + this);
        
        // be defensive...
        if (sessionFactory != null) {
            destroySessionFactory();
        }
        
        scanForMappings();
        buildSessionFactory();
        System.out.println("Hibernate Server started; " + this);
    }

    public void stop() throws Exception {
        System.out.println("Hibernate Server stopping; " + this);
        destroySessionFactory();
        classpathUrls.clear();
    }

    public void destroy() {
        System.out.println("Hibernate Server destroying; " + this);
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    public Session getSession() throws HibernateException {
        Session session = sessionLocal.get();
        if (session == null || !session.isOpen()) {
            session = sessionFactory.openSession();
            sessionLocal.set(session);
        }
        return session;
    }
     
    /**
     * Centralize the logic needed for starting/binding the SessionFactory.
     *
     * @throws Exception
     */
    private void buildSessionFactory() throws Exception {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Centralize the logic needed to unbind/close a SessionFactory.
     *
     * @throws Exception
     */
    private void destroySessionFactory() throws Exception {
        sessionFactory.close();
        sessionFactory = null;
    }

    private void handleMappings(Configuration cfg) throws IOException {
        Iterator itr = classpathUrls.iterator();
        while (itr.hasNext()) {
            final URL url = (URL) itr.next();
            //log.debug("Passing input stream [" + url + "] to Hibernate Configration");
            cfg.addInputStream(url.openStream());
        }
    }

    /**
     * Scan the current context's classloader to locate any potential sources of Hibernate mapping files.
     *
     * @throws DeploymentException
     */
    private void scanForMappings() throws Exception {
        // Won't this cause problems if start() is called from say the console?
        // a way around is to locate our DeploymentInfo and grab its ucl attribute
        // for use here.
        URL[] urls;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl instanceof URLClassLoader) {
            urls = ((URLClassLoader) cl).getURLs();
        } else {
            throw new RuntimeException("Unable to determine urls from classloader [" + cl + "]");
        }
        //扫描urls, 并将.hbm.xml的url加入classpathUrls中
        
    }

    /**
     * Transfer the state represented by our current attribute values into the given Properties instance, translating our
     * attributes into the appropriate Hibernate settings.
     *
     * @param settings The Properties instance to which to add our state.
     */
    private void transferSettings(Properties settings) {

    }

    /**
     * Simple helper method for transferring individual settings to a properties
     * instance only if the setting's value is not null.
     *
     * @param props The properties instance into which to transfer the setting
     * @param key   The key under which to transfer the setting
     * @param value The value of the setting.
     */
    private void setUnlessNull(Properties props, String key, Object value) {

    }
    
    /**
     * 当调用有状态会话服务时, 获得的Session应在多次调用之间被保存
     */
    
}
