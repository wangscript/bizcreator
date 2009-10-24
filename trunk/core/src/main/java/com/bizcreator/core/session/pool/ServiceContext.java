/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

/**
 * 
 * @author lgh
 */
public class ServiceContext {

    /**
     * The Service instance
     */
    Object instance;
    /**
     * Only StatelessSession beans have no Id, stateful and entity do
     */
    Object id;

    public ServiceContext(Object instance) {
        this.instance = instance;
    }
    
    public Object getInstance() {
        return instance;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getId() {
        return id;
    }
    
    public void discard() throws Exception {
        
    }
    
    /**
    * before reusing this context we clear it of previous state called
    * by pool.free()
    */
   public void clear()
   {
      this.id = null;
      /*
      this.locked = 0;
      this.principal = null;
      this.beanPrincipal = null;
      this.synch = null;
      this.transaction = null;
      this.inMethodStack.clear();
       */
   }
}
