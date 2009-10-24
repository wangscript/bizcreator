/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import javax.naming.NamingException;

import org.hibernate.HibernateException;

public interface Initializer extends Remote {

    Object initializeEntity(String className,Serializable id, int token)
    throws RemoteException,
    ClassNotFoundException, 
    HibernateException, 
    NamingException;
    
    Object initializeCollection(String role,Serializable id)
    throws RemoteException,
    ClassNotFoundException, 
    HibernateException,
    NamingException;
    
    
}
