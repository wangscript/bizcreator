/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.impl;

import com.bizcreator.core.BaseException;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.RhSessionContext;
import com.bizcreator.util.ObjectUtil;
import java.util.List;

import com.bizcreator.core.SessionContextImpl;
import com.bizcreator.core.entity.AtomicEntity;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.TooManyRowsAffectedException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * 该类为Hibernate环境下Session bean的基本类
 * @author Administrator
 */
public class HibernateBase extends AbstractSession {

    private boolean stateful = false;
    private HibernateTemplate hibernate;

    public HibernateBase() {
    }

    public HibernateBase(boolean stateful) {
        this.stateful = stateful;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.hibernate = new HibernateTemplate(sessionFactory);
    }

    public SessionFactory getSessionFactory() {
        return hibernate.getSessionFactory();
    }
    
    public Session getSession() {
       return getSessionFactory().getCurrentSession();
    }

    //------------------- 会话环境 -----------------
    protected static SessionContextImpl rhCtx = new SessionContextImpl(null);

    public RhSessionContext getCtx() {
        if (rhCtx == null) {//
            //应该传入一个非空参数进行实例化
            rhCtx = new SessionContextImpl(null);
        }
        return rhCtx;
    }

    //------------------- basic operation for entities ---------------------
    //保存实体
    public void persist(Object entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            Date date = new Date(System.currentTimeMillis());

            if (be.getClientId() == null) {
                be.setClientId(getCtx().getClientId());
            }
            if (be.getOrgId() == null) {
                be.setOrgId(getCtx().getOrgId());
            }
            be.setActive(true);
            be.setCreated(date);
            if (be.getCreatedBy() == null) {
                be.setCreatedBy(getCtx().getUser().getUsername());
            }
            be.setUpdated(date);
            if (be.getUpdatedBy() == null) {
                be.setUpdatedBy(getCtx().getUser().getUsername());
            }
        }
        hibernate.persist(entity);
    }

    //更新/保存实体
    public <T> T merge(T entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            //BasicInfo bi = be.getBi();
            Date date = new Date(System.currentTimeMillis());

            if (be.getClientId() == null) {
                be.setClientId(getCtx().getClientId());
            }
            if (be.getOrgId() == null) {
                be.setOrgId(getCtx().getOrgId());
            }
            be.setActive(true);
            be.setCreated(date);
            if (be.getCreatedBy() == null) {
                be.setCreatedBy(getCtx().getUser().getUsername());
            }

            be.setUpdated(date);
            be.setUpdatedBy(getCtx().getUser().getUsername());
            //be.setBi(bi);
        }
        return (T) hibernate.merge(entity);
    }

    //删除实体
    public void remove(AtomicEntity entity) {
        hibernate.delete(entity);
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
        Object toRemove = hibernate.get(entity.getClass(), (Serializable) id);
        remove((AtomicEntity) toRemove);
    }

    public void remove(Class clazz, Object id) {
        Object toRemove = hibernate.get(clazz, (Serializable) id);
        remove((AtomicEntity) toRemove);
    }

    //根据主键查找实体
    public <A> A find(Class<A> clazz, Object primaryKey) {
        return (A) hibernate.get(clazz, (Serializable) primaryKey);
    }

    protected Query createQuery(String qlString, boolean filterByClients) {
        if (filterByClients) {
            int index = qlString.indexOf("where");
            if (index == -1) {
                index = qlString.indexOf("WHERE");
            }
            if (index > 0) {
                qlString = qlString.substring(0, index + 5) + " o.clientId in (" + getClients() + ") and " + qlString.substring(index + 5);
            }
            //qlString = qlString + " and o.clientId in (" + getClients() + ")";
        }
        
        final String qls = qlString;

        Query query = (Query) hibernate.execute(new HibernateCallback() {
            public Object doInHibernate(Session session) {
                return session.createQuery(qls);
            }
        });
        
        if (fetchSize > 0) {
            query.setFetchSize(fetchSize);
        }
        return query;
    }

    /**
     * 构建查询，并添加client约束
     * @param qlString
     * @return
     */
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
                query.setParameter(i, parseParam(params[i]));
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
                if (key.equals("_in")) {
                    Object param = paramMap.get(key);
                    if (param instanceof Object[]) {
                        query.setParameterList(key, (Object[]) param);
                    } else if (param instanceof Collection) {
                        query.setParameterList(key, (Collection) param);
                    }
                } else {
                    query.setParameter(key, parseParam(paramMap.get(key)));
                }
            }
        }
    }

    /**
     * 创建带名字参数的查询
     * @param qlString
     * @param params
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
                String key = (String) paramPairs[i];
                if (key.endsWith("_in")) {
                    Object param = paramPairs[i + 1];
                    if (param instanceof Object[]) {
                        query.setParameterList(key, (Object[]) param);
                    } else if (param instanceof Collection) {
                        query.setParameterList(key, (Collection) param);
                    }
                } else {
                    query.setParameter(key, parseParam(paramPairs[i + 1]));
                }
            }
        }
    }

    //--------------------------------------------------------------
    //执行查询, 返回列表
    public List executeQuery(String qlString) {
        return createQuery(qlString).list();
    }

    //执行带参数的查询
    public List executeQuery(String qlString, Object[] params) {
        return createQuery(qlString, params).list();
    }

    public List executeQuery(String qlString, Map<String, Object> paramMap) {
        return createQuery(qlString, paramMap).list();
    }

    public List executeNpQuery(String qlString, Object[] paramPairs) {
        return createNpQuery(qlString, paramPairs).list();
    }

    //------------------------------------------------
    //执行查询,返回单个值
    public Object getSingleResult(String qlString) {
        return createQuery(qlString).uniqueResult();
    }

    public Object getSingleResult(String qlString, Object[] params, boolean filterByClients) {
        Query query = createQuery(qlString, filterByClients);
        buildQuery(query, params);
        return query.uniqueResult();
    }

    public Object getSingleResult(String qlString, Object[] params) {
        Query query = createQuery(qlString);
        buildQuery(query, params);
        return query.uniqueResult();
    }

    public Object getSingleResult(String qlString, Map<String, Object> paramMap) {
        Query query = createQuery(qlString);
        buildQuery(query, paramMap);
        return query.uniqueResult();
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs, boolean filterByClients) {
        Query query = createQuery(qlString, filterByClients);
        buildNpQuery(query, paramPairs);
        return query.uniqueResult();
    }

    public Object getNpSingleResult(String qlString, Object[] paramPairs) {
        return getNpSingleResult(qlString, paramPairs, true);
    }

    //---------------------------------------------------------------
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

    //-------------------------------------------------------
    public ScrollableResults scroll(String qlString) {
        return createQuery(qlString).scroll();
    }

    public ScrollableResults scroll(String qlString, Object[] params) {
        return createQuery(qlString, params).scroll();
    }

    public ScrollableResults scroll(String qlString, Map<String, Object> paramMap) {
        return createQuery(qlString, paramMap).scroll();
    }

    public ScrollableResults scrollNp(String qlString, Object[] paramPairs) {
        return createNpQuery(qlString, paramPairs).scroll();
    }

    /*
    public String login(String name, String password) {
    return name;
    }
     */
    @Override
    public void handleException(Exception ex, Object data) {

        //应将ex 转换成BaseException, 使其message本地化
        if (ex instanceof NonUniqueObjectException) {
            throw (NonUniqueObjectException) ex;
        } else if (ex instanceof NonUniqueResultException) {
            throw (NonUniqueResultException) ex;
        } else if (ex instanceof QueryException) {
            throw (QueryException) ex;
        } else if (ex instanceof TooManyRowsAffectedException) {
            throw (TooManyRowsAffectedException) ex;
        } else {
            super.handleException(ex, data);
        }
    }
}
