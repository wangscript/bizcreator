/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.hibernate.collection.Remotable;
import com.bizcreator.core.session.LazyLoading;
import java.io.Serializable;
import javax.ejb.Stateless;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.CollectionKey;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author lgh
 */
@Stateless
@Transactional
public class LazyLoadingSession extends BasicSession implements LazyLoading {
    //private static final long serialVersionUID = -3354876500675131726L;
    //private static  SessionFactory factory = new Configuration().configure().buildSessionFactory();
    
    static Log log = LogFactory.getLog(LazyLoadingSession.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Object initializeEntity(String className,Serializable id, int token) 
    throws ClassNotFoundException, HibernateException {
        /*
        Class cls = Class.forName(className,false,Thread.currentThread().getContextClassLoader());
        Session currentSession = factory.getCurrentSession();
        if(System.identityHashCode(currentSession) == token){
            Object obj = currentSession.get(cls,id);
            if(obj == null){
                throw new IllegalStateException("object not found " + cls + " " + id);
            }
         return ((HibernateProxy)obj).getHibernateLazyInitializer().getImplementation();
        }else {
            throw new IllegalStateException("invalid session id");
        }
        */
        //Class cls = Class.forName(className,false,Thread.currentThread().getContextClassLoader());
        Class cls = Class.forName(className);
        log.info(">>>entity class: " + className + ", id: " + id + ", token: " + token);
        Object obj = find(cls, id);
        if(obj == null){
            throw new IllegalStateException("object not found " + cls + " " + id);
        }
        if (obj instanceof HibernateProxy) {
            return ((HibernateProxy)obj).getHibernateLazyInitializer().getImplementation(); 
        } else {
            return obj;
        }
    }

    public Object initializeCollection(String role,Serializable id)
            throws ClassNotFoundException, HibernateException {
        
        //HibernateBase hibernateBase = (HibernateBase) serviceBase;
        /*
        if (serviceBase instanceof HibernateBase) {
            hibernateBase = (HibernateBase) serviceBase;
        }
        else {
            throw new RuntimeException("Not support this operation in EJB3 envirnment!");
        }*/
        
        log.debug(">>>init collection: role = " + role + ", id = " + id);
        
        PersistenceContext context = ((SessionImplementor) sessionFactory.getCurrentSession()).getPersistenceContext();
        CollectionPersister persister = ((SessionFactoryImplementor)sessionFactory).getCollectionPersister(role);
        CollectionKey key = new CollectionKey(persister,id,EntityMode.POJO);
        Remotable col = (Remotable) context.getCollection(key);
        
        if(col == null){
            //role: com.rhinofield.base.entity.MenuModel.functions
            int lastIndex = role.lastIndexOf(".");
            
            //get class name: com.rhinofield.base.entity.MenuModel
            String className = role.substring(0, lastIndex);
            sessionFactory.getCurrentSession().get(Class.forName(className), id);
            col = (Remotable) context.getCollection(key);
        }
        
        if (col == null) {
            throw new IllegalStateException("Collection not found!");
        }
        
        Object items = col.unwrap();
        return items;
    }
}
