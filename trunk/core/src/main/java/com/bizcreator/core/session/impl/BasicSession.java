/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.impl;

import com.bizcreator.core.BaseException;
import com.bizcreator.core.BizContext;
import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.session.ServiceBase;
import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.ScrollableResults;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Stateless
@Transactional
public class BasicSession extends AbstractSession implements ServiceBase {

    @PersistenceContext
    protected EntityManager em;
    
    //@Resource
    SessionContext ejbSessionCtx;
    protected ServiceBase serviceBase = null;

    public BasicSession() {
        /*
        if (RhinoCtx.SERVICE_EJB3.equals(rhinoContext.getServiceType())) {
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
        } else if (RhinoCtx.SERVICE_HIBERNATE.equals(rhinoContext.getServiceType())) {
            serviceBase = new HibernateBase();
        } else if (RhinoCtx.SERVICE_SEAM.equals(rhinoContext.getServiceType())) {
            throw new UnsupportedOperationException("Not supported yet.");
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }*/
    }

    public void setServiceBase(ServiceBase serviceBase) {
        this.serviceBase = serviceBase;
    }
    
    //-------------------------- 通过serviceBase实现ServiceBase接口 ----------------------
    public RhSessionContext getCtx() {
        return serviceBase.getCtx();
    }

    public void persist(Object entity) {
        try {
            if (entity instanceof AtomicEntity) {
                ((AtomicEntity) entity).beforeSave();
                ((AtomicEntity) entity).beforePersist();

            }
            serviceBase.persist(entity);
            if (entity instanceof AtomicEntity) {
                ((AtomicEntity) entity).afterPersist();
                ((AtomicEntity) entity).afterSave();
            }
        } catch (Exception ex) {
            handleException(ex, entity);
        }
    }

    public <T> T merge(T entity) {
        T result = null;
        try {
            if (entity instanceof AtomicEntity) {
                ((AtomicEntity) entity).beforeSave();
                ((AtomicEntity) entity).beforeMerge();
            }
            result = serviceBase.merge(entity);
            if (entity instanceof AtomicEntity) {
                ((AtomicEntity) entity).afterMerge();
                ((AtomicEntity) entity).afterSave();
            }
        } catch (Exception ex) {
            handleException(ex, entity);
        }
        return result;
    }

    public void remove(AtomicEntity entity) {
        //
        if (!entity.isServiced()) {
            //根据ServiceInfo标记查找服务来完成操作
            Class clazz = entity.getClass();
            ServiceBase service = getEntityService(clazz);
            if (service != null) {

                entity.setServiced(true);
                service.remove(entity);

                return;
            }
        }

        try {
            entity.beforeRemove();
            serviceBase.remove(entity);
            entity.afterRemove();
        } catch (Exception ex) {
            handleException(ex, entity);
        }
    }

    public void removeFromClient(AtomicEntity entity) {
        try {
            serviceBase.removeFromClient(entity);
        } catch (Exception ex) {
            handleException(ex, entity);
        }
    }

    public void remove(Class entityClass, Object primaryKey) {
        try {
            Object entity = find(entityClass, primaryKey);
            if (entity instanceof AtomicEntity) {
                remove((AtomicEntity) entity);
            } else {
                serviceBase.remove(entityClass, primaryKey);
            }
        } catch (Exception ex) {
            handleException(ex, null);
        }
    }

    public <A> A find(Class<A> entityClass, Object primaryKey) {
        try {
            return serviceBase.find(entityClass, primaryKey);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleException(ex, null);
        }
        return null;
    }

    //-------------- 执行查询 ------------------
    public List executeQuery(String qlString) {
        try {
            return serviceBase.executeQuery(qlString);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public List executeQuery(String qlString, Object[] params) {
        try {
            return serviceBase.executeQuery(qlString, params);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public List executeQuery(String qlString, Map<String, Object> paramMap) {
        try {
            return serviceBase.executeQuery(qlString, paramMap);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public List executeNpQuery(String qlString, Object[] paramPairs) {
        try {
            return serviceBase.executeNpQuery(qlString, paramPairs);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    

    //--------------------- 取单个对象 ---------------------
    public Object getSingleResult(String qlString) {
        try {
            return serviceBase.getSingleResult(qlString);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public Object getSingleResult(String qlString, Object[] params, boolean filterByClients) {
        try {
            return serviceBase.getSingleResult(qlString, params, filterByClients);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public Object getSingleResult(String qlString, Object[] params) {
        try {
            return serviceBase.getSingleResult(qlString, params);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public Object getSingleResult(String qlString, Map<String, Object> paramMap) {
        try {
            return serviceBase.getSingleResult(qlString, paramMap);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs, boolean filterByClients) {
        try {
            return serviceBase.getNpSingleResult(qlString, paramPairs, filterByClients);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs) {
        try {
            return serviceBase.getNpSingleResult(qlString, paramPairs);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    //---------------------- 根据命名查询获取单个结果 ---------
    

    //----------------------- 执行更新 ------------------------
    public int executeUpdate(String qlString) {
        try {
            return serviceBase.executeUpdate(qlString);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return 0;
    }

    public int executeUpdate(String qlString, Object[] params) {
        try {
            return serviceBase.executeUpdate(qlString, params);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return 0;
    }

    public int executeUpdate(String qlString, Map<String, Object> paramMap) {
        try {
            return serviceBase.executeUpdate(qlString, paramMap);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return 0;
    }

    public int executeNpUpdate(String qlString, Object[] paramPairs) {
        try {
            return serviceBase.executeNpUpdate(qlString, paramPairs);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return 0;
    }

    //-------------------------- 根据named query执行更新 ----------------
    

    //-----------------------------------------------------------------------
    public ScrollableResults scroll(String qlString) {
        try {
            return serviceBase.scroll(qlString);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public ScrollableResults scroll(String qlString, Object[] params) {
        try {
            return serviceBase.scroll(qlString, params);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public ScrollableResults scroll(String qlString, Map<String, Object> paramMap) {
        try {
            return serviceBase.scroll(qlString, paramMap);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    public ScrollableResults scrollNp(String qlString, Object[] paramPairs) {
        try {
            return serviceBase.scrollNp(qlString, paramPairs);
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return null;
    }

    //----------------------------------------------------------
    @Override
    public void handleException(Exception ex, Object data) {
        serviceBase.handleException(ex, data);
    }

    //------------------------- 实用方法 ----------------------
    public double doubleValue(Object value) {
        return value == null ? 0.00 : (Double) value;
    }
    /**
     * 该方法仅供测试
     * @param name
     * @param password
     * @return
     */
    /*
    public String login(String name, String password) {
    return serviceBase.login(name, password);
    }
     */
}
