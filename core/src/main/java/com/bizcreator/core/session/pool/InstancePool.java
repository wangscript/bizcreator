/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;

/**
 * Defines the model for a EnterpriseContext instance pool.
 *
 * @author <a href="mailto:rickard.oberg@telkel.com">Rickard ?erg</a>
 * @version $Revision: 1.2 $
 */
public interface InstancePool {
    /**
    * Get an instance without identity.
    *
    * <p>Can be used by finders and create-methods, or stateless beans
    *
    * @return    Context/w instance
    *
    * @throws Exception    RemoteException
    */
   ServiceContext get() throws Exception;

   /**
    * Return an anonymous instance after invocation.
    *
    * @param ctx    The context to free.
    */
   void free(ServiceContext ctx);

   /**
    * Discard an anonymous instance after invocation.
    * This is called if the instance should not be reused, perhaps due to some
    * exception being thrown from it.
    *
    * @param ctx    The context to discard.
    */
   void discard(ServiceContext ctx);

   /**
    * Return the size of the pool.
    *
    * @return the size of the pool.
    */
   int getCurrentSize();

   /**
    * Get the maximum size of the pool.
    *
    * @return the size of the pool.
    */
   public int getMaxSize();
}
