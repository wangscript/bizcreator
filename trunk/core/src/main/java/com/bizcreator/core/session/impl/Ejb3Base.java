/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.impl;

import com.bizcreator.core.BaseException;
import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.SessionContextImpl;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;

import com.bizcreator.util.ObjectUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.SessionContext;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import org.hibernate.ScrollableResults;

/**
 *
 * @author Administrator
 */
public abstract class Ejb3Base extends AbstractSession {

    protected RhSessionContext rhCtx = null;

    public abstract EntityManager getEm();

    public abstract SessionContext getEjbSessionCtx();

    public RhSessionContext getCtx() {
        if (rhCtx == null) {//
            rhCtx = new SessionContextImpl(getEjbSessionCtx());
        }
        return rhCtx;
    }

    //------------------- basic operation for entities ---------------------
    //保存实体
    public void persist(Object entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            //BasicInfo bi = be.getBi() == null ? new BasicInfo() : be.getBi();
            Date date = new Date(System.currentTimeMillis());
            be.setClient_id(getCtx().getClient_id());
            be.setOrg_id(getCtx().getOrg_id());
            be.setIs_active(true);
            be.setCreated(date);
            be.setCreated_by(getCtx().getUser().getUsername());
            be.setUpdated(date);
            be.setUpdated_by(getCtx().getUser().getUsername());

            //be.setBi(bi);
        }
        getEm().persist(entity);
    }

    //更新/保存实体
    public <T> T merge(T entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            //BasicInfo bi = be.getBi();
            Date date = new Date(System.currentTimeMillis());

            if (be.getClient_id() == null) {
                be.setClient_id(getCtx().getClient_id());
                be.setOrg_id(getCtx().getOrg_id());
                be.setIs_active(true);
                be.setCreated(date);
                be.setCreated_by(getCtx().getUser().getUsername());
            }
            be.setUpdated(date);
            be.setUpdated_by(getCtx().getUser().getUsername());
            //be.setBi(bi);
        }
        return (T) getEm().merge(entity);
    }

    //删除实体
    public void remove(AtomicEntity entity) {
        Object toRemove = getEm().merge(entity);
        getEm().remove(toRemove);
    }

    /**
     * 删除从远程客户端传来的detached entity, 不鼓励使用该方法, 
     * 应该使用: remove(Class clazz, Object id), 效率更高
     */
    public void removeFromClient(AtomicEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Can't remove a null entity!");
        }
        Object id = ObjectUtil.getFieldValue(entity, "id");
        if (id == null) {
            id = ObjectUtil.getFieldValue(entity, "pk");
        }
        if (id == null) {
            throw new BaseException("The entity hasn't a identifier, can't remove it!");
        }
        Object toRemove = getEm().find(entity.getClass(), id);
        getEm().remove(toRemove);
    }

    public void remove(Class clazz, Object id) {
        Object toRemove = getEm().find(clazz, id);
        getEm().remove(toRemove);
    }

    //根据主键查找实体
    public <A> A find(Class<A> entityClass, Object primaryKey) {
        return (A) getEm().find(entityClass, (Serializable) primaryKey);
    }

    protected Query createQuery(String qlString, boolean filterByClients) {
        //qlString = qlString + " and o.client_id=:client_id";
        Query query = getEm().createQuery(qlString);
        //query.setParameter("client_id", getCtx().getClient_id());
        return query;
    }

    //创建查询
    protected Query createQuery(String qlString) {
        return createQuery(qlString, true);
    }

    protected Query createQuery(String qlString, Object[] params) {
        Query query = createQuery(qlString);
        buildQuery(query, params);
        return query;
    }

    protected void buildQuery(Query query, Object[] params) {
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, parseParam(params[i]));
            }
        }
    }

    protected Query createQuery(String qlString, Map<String, Object> paramMap) {
        Query query = createQuery(qlString);
        buildQuery(query, paramMap);
        return query;
    }

    protected void buildQuery(Query query, Map<String, Object> paramMap) {
        Set<String> keys = paramMap.keySet();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                query.setParameter(key, parseParam(paramMap.get(key)));
            }
        }
    }

    /**
     * create named param query
     * @param qlString
     * @param paramPairs
     * @return
     */
    protected Query createNpQuery(String qlString, Object[] paramPairs) {
        Query query = createQuery(qlString);
        buildNpQuery(query, paramPairs);
        return query;
    }

    protected void buildNpQuery(Query query, Object[] paramPairs) {
        if (paramPairs != null && paramPairs.length > 0) {
            for (int i = 0; i < paramPairs.length; i = i + 2) {
                query.setParameter((String) paramPairs[i], parseParam(paramPairs[i + 1]));
            }
        }
    }

    //执行查询, 返回列表
    public List executeQuery(String qlString) {
        return createQuery(qlString).getResultList();
    }

    //执行带参数的查询
    public List executeQuery(String qlString, Object[] params) {
        return createQuery(qlString, params).getResultList();
    }

    public List executeQuery(String qlString, Map<String, Object> paramMap) {
        return createQuery(qlString, paramMap).getResultList();
    }

    public List executeNpQuery(String qlString, Object[] paramPairs) {
        return createNpQuery(qlString, paramPairs).getResultList();
    }

    //执行查询,返回单个值
    public Object getSingleResult(String qlString) {
        Query query = getEm().createQuery(qlString);
        return query.getSingleResult();
    }

    public Object getSingleResult(String qlString, Object[] params, boolean filterByClients) {
        Query query = createQuery(qlString, filterByClients);
        buildQuery(query, params);
        return query.getSingleResult();
    }

    public Object getSingleResult(String qlString, Object[] params) {
        Query query = createQuery(qlString);
        buildQuery(query, params);
        return query.getSingleResult();
    }

    public Object getSingleResult(String qlString, Map<String, Object> paramMap) {
        Query query = getEm().createQuery(qlString);
        buildQuery(query, paramMap);
        return query.getSingleResult();
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs, boolean filterByClients) {
        Query query = createQuery(qlString, filterByClients);
        buildNpQuery(query, paramPairs);
        return query.getSingleResult();
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs) {
        return getNpSingleResult(qlString, paramPairs, true);
    }

    //执行更新
    public int executeUpdate(String qlString) {
        return createQuery(qlString).executeUpdate();
    }

    public int executeUpdate(String qlString, Object[] params) {
        return createQuery(qlString, params).executeUpdate();
    }

    public int executeUpdate(String qlString, Map<String, Object> paramMap) {
        return createQuery(qlString, paramMap).executeUpdate();
    }

    public int executeNpUpdate(String qlString, Object[] paramPairs) {
        return createNpQuery(qlString, paramPairs).executeUpdate();
    }

    public ScrollableResults scroll(String qlString) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ScrollableResults scroll(String qlString, Object[] params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ScrollableResults scroll(String qlString, Map<String, Object> paramMap) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ScrollableResults scrollNp(String qlString, Object[] paramPairs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 该方法仅供测试
     * @param name
     * @param password
     * @return
     */
    /*
    public String login(String name, String password) {
    return name;
    }
     */
    @Override
    public void handleException(Exception ex, Object data) {

        if (ex instanceof EntityExistsException) {
            throw (EntityExistsException) ex;
        } else if (ex instanceof EntityNotFoundException) {
            throw (EntityNotFoundException) ex;
        } else if (ex instanceof NonUniqueResultException) {
            throw (NonUniqueResultException) ex;
        } else if (ex instanceof NoResultException) {
            throw (NoResultException) ex;
        } else {
            super.handleException(ex, data);
        }
    }
}
