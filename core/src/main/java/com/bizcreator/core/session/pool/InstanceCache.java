/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;
import com.bizcreator.util.CachePolicy;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;


/**
 *
 * @author lgh
 */
public interface InstanceCache {
    
    
    public void setCachePolicy(CachePolicy cachePolicy);
    
       /**
    * Gets a bean instance from this cache given the identity.  This method
    * may involve activation if the instance is not in the cache.
    * 
    * <p>Implementation should have O(1) complexity.
    * 
    * <p>This method is never called for stateless session beans.
    *
    * @param id    The primary key of the bean .
    * @return      The EnterpriseContext related to the given id.
    * 
    * @throws RemoteException          In case of illegal calls (concurrent /
    *                                  reentrant)
    * @throws NoSuchObjectException    if the bean cannot be found.
    *                            
    * @see #release
    */
   ServiceContext get(Object id)
      throws RemoteException, NoSuchObjectException;

   /**
    * Inserts an active bean instance after creation or activation.
    * 
    * <p>Implementation should guarantee proper locking and O(1) complexity.
    *
    * @param ctx    The EnterpriseContext to insert in the cache
    * 
    * @see #remove
    */
   void insert(ServiceContext ctx);

   /**
    * Releases the given bean instance from this cache.
    * This method may passivate the bean to get it out of the cache.
    * Implementation should return almost immediately leaving the
    * passivation to be executed by another thread.
    *
    * @param ctx    The EnterpriseContext to release
    * 
    * @see #get
    */
   void release(ServiceContext ctx);

   /**
    * Removes a bean instance from this cache given the identity.
    * Implementation should have O(1) complexity and guarantee proper locking.
    *
    * @param id    The pimary key of the bean.
    * 
    * @see #insert
    */
   void remove(Object id);

   /**
    * Checks whether an instance corresponding to a particular id is active.
    *
    * @param id    The pimary key of the bean.
    * 
    * @see #insert
    */
   boolean isActive(Object id);

   /** Get the current cache size
    *
    * @return the size of the cache
    */
   long getCacheSize();
   /** Flush the cache.
    *
    */
   void flush();
   
   
}
