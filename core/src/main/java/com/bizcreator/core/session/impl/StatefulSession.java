/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.BizContext;
import com.bizcreator.core.session.StatefulService;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;

/**
 * 有状态的会话服务
 * @author lgh
 */
public class StatefulSession extends BasicSession implements StatefulService {

    public StatefulSession() {
        if (BizContext.SERVICE_EJB3.equals(rhinoContext.getServiceType())) {
            serviceBase = new Ejb3Base() {
                @Override
                public EntityManager getEm() {
                    return em;
                }

                @Override
                public SessionContext getEjbSessionCtx() {
                    return ejbSessionCtx;
                }
            };
        }
        else if (BizContext.SERVICE_HIBERNATE.equals(rhinoContext.getServiceType())) {
            serviceBase = new HibernateBase(true);
        }
        else if (BizContext.SERVICE_SEAM.equals(rhinoContext.getServiceType())) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    
    //------------------- 生命周期方法 ------------------
    public void create() {
        
    }

    public void activate() {
        
    }

    public void passivate() {
        closeSession();
    }

    public void destroy() {
        closeSession();
    }

    
    public void startTx() {
        if (serviceBase instanceof HibernateBase) {
            //((HibernateBase)serviceBase).startTx();
        }
        else if (serviceBase instanceof Ejb3Base) {
            
        }
    }
    
    public void commit() {
        if (serviceBase instanceof HibernateBase) {
            //((HibernateBase)serviceBase).commit();
        }
        else if (serviceBase instanceof Ejb3Base) {
            
        }
    }

    public void rollback() {
        if (serviceBase instanceof HibernateBase) {
            //((HibernateBase)serviceBase).rollback();
        }
        else if (serviceBase instanceof Ejb3Base) {
            
        }
    }
    
    protected void closeSession() {
        if (serviceBase instanceof HibernateBase) {
            //((HibernateBase)serviceBase).closeSession();
        }
        else if (serviceBase instanceof Ejb3Base) {
            
        }
    }

    
}
