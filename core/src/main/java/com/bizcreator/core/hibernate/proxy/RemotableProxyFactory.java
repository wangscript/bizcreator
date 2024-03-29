/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.type.AbstractComponentType;

/**
 * Custom loader factory
 * @author Juozas
 *
 */
public class RemotableProxyFactory implements ProxyFactory {

    protected static final Class[] NO_CLASSES = new Class[0];

    private Class persistentClass;
    private String entityName;
    private Class[] interfaces;
    private Method getIdentifierMethod;
    private Method setIdentifierMethod;
    private AbstractComponentType componentIdType;
    private Class factory;

    public void postInstantiate(
        final String entityName,
        final Class persistentClass,
        final Set interfaces,
        final Method getIdentifierMethod,
        final Method setIdentifierMethod,
        AbstractComponentType componentIdType)
    throws HibernateException {
        this.entityName = entityName;
        this.persistentClass = persistentClass;
        this.interfaces = (Class[]) interfaces.toArray(NO_CLASSES);
        this.getIdentifierMethod = getIdentifierMethod;
        this.setIdentifierMethod = setIdentifierMethod;
        this.componentIdType = componentIdType;
        factory = RemotableLazyInitializer.getProxyFactory(persistentClass, this.interfaces);
    }

    public HibernateProxy getProxy(Serializable id, SessionImplementor session)
        throws HibernateException {

        return RemotableLazyInitializer.getProxy(
                factory, 
                entityName, 
                persistentClass, 
                interfaces, 
                getIdentifierMethod, 
                setIdentifierMethod,
                componentIdType,
                id, 
                session
            );
    }

}

