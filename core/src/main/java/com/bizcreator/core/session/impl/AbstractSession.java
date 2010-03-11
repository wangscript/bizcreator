/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.ServiceBase;
import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.BizContext;

import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.EntryEntity;
import com.bizcreator.util.ObjectUtil;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author lgh
 */
@Transactional
public abstract class AbstractSession implements ServiceBase {

    protected static Log log = LogFactory.getLog(AbstractSession.class);
    protected int fetchSize = 0;
    protected BizContext rhinoContext = BizContext.instance();

    public abstract RhSessionContext getCtx();

    public String getDomain() {
        return BizContext.instance().getDomain();
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /**
     * 去除clazz的代理后缀, 返回clazz实际的类名，如：
     * com.rhinofield.tms.general.entity.DeliveryStop$ServerProxy -->
     * com.rhinofield.tms.general.entity.DeliveryStop
     * @param clazz
     * @return
     */
    public String getClassName(Class clazz) {
        String className = clazz.getName();
        int sepPos = className.indexOf("$");
        if (sepPos > 0) {
            className = className.substring(0, sepPos);
        }
        return className;
    }

    public Object getService(String serviceName) {
        //return rhinoContext.getServiceLocator().lookup(serviceName);
        try {
            return ServiceFactory.getService(serviceName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ServiceBase getEntityService(Class entityClass) {
        try {
            return ServiceFactory.getEntityService(entityClass);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public <A> A findByCode(Class<A> clazz, String code) {
        StringBuffer sb = new StringBuffer("from " + clazz.getName() + " o where o.code=:code");
        return (A) getNpSingleResult(sb.toString(), new Object[]{"code", code});
    }

    public <A> A findByName(Class<A> clazz, String name) {
        StringBuffer sb = new StringBuffer("from " + clazz.getName() + " o where o.name=:name");
        return (A) getNpSingleResult(sb.toString(), new Object[]{"name", name});
    }

    //创建实体//
    public <T extends AtomicEntity> T create(T entity) {
        //System.out.println(">>>create entity: " + entity.getName());
        return save(entity);
    }

    public <T extends AtomicEntity> T update(T entity) {
        return save(entity);
    }


    @Transactional
    public <T extends AtomicEntity> T save(T master, Collection<? extends EntryEntity> ... entries) {
        save(master);
        for (int i=0; i<entries.length; i++) {
            for (EntryEntity ae : entries[i]) {
                if (ae.getMaster() == null) {
                    ae.setMaster(master);
                }
                save(ae);
            }
        }
        return master;
    }

    @Transactional
    public <T extends AtomicEntity> T save(T entity) {
        if (!entity.isServiced()) {
            //根据ServiceInfo标记查找服务
            Class clazz = entity.getClass();
            ServiceBase service = getEntityService(clazz);
            if (service != null) {
                entity.setServiced(true);
                service.save(entity);
                return entity;
            }
        }

        entity.setServiced(false);
        if (entity.getId() == null) {
            persist(entity);
        } else {
            merge(entity);
        }

        return entity;
    }

    @Transactional
    public <T extends AtomicEntity> void create(T[] entities) {
        if (entities != null) {
            for (T entity : entities) {
                create(entity);
            }
        }
    }

    @Transactional
    public <T extends AtomicEntity> void update(T[] entities) {
        if (entities != null) {
            for (T entity : entities) {
                update(entity);
            }
        }
    }

    //创建/更新多个实体
    @Transactional
    public <T extends AtomicEntity> T[] save(T[] entities) {
        int len = 0;
        if (entities == null) {
            return null;
        }
        len = entities.length;
        T[] results = (T[]) new Object[len];
        int i = 0;
        for (T entity : entities) {
            results[i++] = save(entity);
        }
        return results;
    }

    //删除多个实体
    @Transactional
    public void remove(Class entityClass, Object[] ids) {
        for (Object id : ids) {
            if (id != null) {
                Object entity = find(entityClass, (Serializable) id);
                remove((AtomicEntity) entity);
            }
        }
    }

    protected Object parseParam(Object param) {
        Object result = param;
        if ("$client_id".equals(param)) {
            result = getCtx().getClient_id();
        } else if ("$org_id".equals(param)) {
            result = getCtx().getOrg_id();
        } else if ("$dutyId".equals(param)) {
            result = getCtx().getDutyId();
        } else if (param instanceof String && ((String) param).startsWith("$")) {
            String key = ((String) param).substring(1);
            result = getCtx().get(key);
            System.out.println(">>>parse param: " + key + ", " + result);
        }
        return result;
    }

    protected String getClients() {
        String clients = "'0'";
        String client_id = getCtx().getClient_id();
        if (!"0".equals(client_id)) {
            clients = "'" + client_id + "'," + clients;
        }
        return clients;
    }
/*
    protected NamedQueries getNamedQueries() {
        NamedQueries nq = (NamedQueries) rhinoContext.get(BizContext.NAMED_QUERIES);
        if (nq == null) {
            String className = rhinoContext.getInitParameter(BizContext.NAMED_QUERIES_CLASS);
            if (className != null) {
                try {
                    Class clazz = Class.forName(className);
                    nq = (NamedQueries) clazz.newInstance();
                    rhinoContext.set(BizContext.NAMED_QUERIES, nq);
                } catch (InstantiationException ex) {
                    log.error(null, ex);
                } catch (IllegalAccessException ex) {
                    log.error(null, ex);
                } catch (ClassNotFoundException ex) {
                    log.error(null, ex);
                }
            }
        }
        return nq;
    }
*/
    public List executeQuery(String qlSelect, Class fromClass, String filter, Object[] paramPairs) {
        String ql = QueryFactory.getQL(qlSelect, fromClass, filter, paramPairs);
        return executeNpQuery(ql, paramPairs);
    }

    public List executeQuery(String qlSelect, Class fromClass, Object[] paramPairs) {
        return executeQuery(qlSelect, fromClass, null, paramPairs);
    }

    //--------------------------------------------------------
    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String extraFrom, String filter, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, extraFrom, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String extraFrom, String filter, Map paramMap) {
        //1. 构造查询语句
        Object[] paramPairs = ObjectUtil.fromParamMap(paramMap);
        String ql = QueryFactory.getQL(qeClass, fromClass, extraFrom, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String filter, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String filter, Map paramMap) {
        //1. 构造查询语句
        Object[] paramPairs = ObjectUtil.fromParamMap(paramMap);
        String ql = QueryFactory.getQL(qeClass, fromClass, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, String filter, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, qeClass, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, String filter, Map paramMap) {
        //1. 构造查询语句
        Object[] paramPairs = ObjectUtil.fromParamMap(paramMap);
        String ql = QueryFactory.getQL(qeClass, qeClass, filter, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, Map paramMap) {
        //1. 构造查询语句
        Object[] paramPairs = ObjectUtil.fromParamMap(paramMap);
        String ql = QueryFactory.getQL(qeClass, fromClass, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Map paramMap) {
        //1. 构造查询语句
        Object[] paramPairs = ObjectUtil.fromParamMap(paramMap);
        String ql = QueryFactory.getQL(qeClass, paramPairs);
        List<A> list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass) {
        return queryEntities(qeClass, fromClass, (Object[]) null);
    }

    //--------------------------- 按字段查询数据 ---------------------------
    public List queryDataList(String[] fields, Class qeClass, Class fromClass, String extraFrom,
            String filter, Object[] paramPairs) {
        String ql = QueryFactory.getQL(fields, qeClass, fromClass, extraFrom, filter, paramPairs);
        List list = executeNpQuery(ql, paramPairs);
        return list;
    }

    public List queryDataList(String[] fields, Class qeClass, Class fromClass, String filter,
            Object[] paramPairs) {
        String ql = QueryFactory.getQL(fields, qeClass, fromClass, filter, paramPairs);
        List list = executeNpQuery(ql, paramPairs);
        return list;
    }

    public List queryDataList(String[] fields, Class fromClass, String filter, Object[] paramPairs) {
        String ql = QueryFactory.getQL(fields, fromClass, filter, paramPairs);
        List list = executeNpQuery(ql, paramPairs);
        return list;
    }

    //--------------------------- search entities -------------------------
    public Object[] searchEntities(Class qeClass, Class fromClass, String param) {
        return searchEntities(qeClass, fromClass, param, null);
    }

    /**
     * 用于RhLookupField的按代码等搜索字段进行查找
     * @param qeClass
     * @param fromClass
     * @param param
     * @return
     */
    public Object[] searchEntities(Class qeClass, Class fromClass, String param, Object[] paramPairs) {
        List<String> likeFields = QueryFactory.getLikeFields(qeClass);
        Object[] params;
        if (paramPairs != null && paramPairs.length > 0) {
            params = new Object[paramPairs.length + 2];
            System.arraycopy(paramPairs, 0, params, 2, paramPairs.length);
        } else {
            params = new Object[2];
        }

        Object[] results = new Object[3];
        if (likeFields != null && likeFields.size() > 0) {
            for (String f : likeFields) {
                params[0] = f;
                params[1] = "%" + param + "%";
                List list = queryEntities(qeClass, fromClass, params);
                if (list != null && list.size() > 0) {
                    results[0] = f;
                    results[1] = param;
                    results[2] = list;
                    break;
                }
            }
        }
        return results;
    }

    //------------------ query atomics --------------------------------
    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass, String filter, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, filter, paramPairs, "atomic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, paramPairs, "atomic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<AtomicEntity> queryAtomics(Class qeClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, paramPairs, "atomic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass) {
        return queryAtomics(qeClass, fromClass, null);
    }

    //------------------ query basic entities --------------------------------
    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass, String filter, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, filter, paramPairs, "basic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, fromClass, paramPairs, "basic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<BasicEntity> queryBasicEntities(Class qeClass, Object[] paramPairs) {
        //1. 构造查询语句
        String ql = QueryFactory.getQL(qeClass, paramPairs, "basic");
        List list = executeNpQuery(ql, paramPairs);

        //4. 返回结果
        return list;
    }

    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass) {
        return queryBasicEntities(qeClass, fromClass, null);
    }

    /**
     * 返回报表输入流
     * @param reportName
     * @return
     */
    public InputStream getReportStream(String reportName) {
        if (reportName == null) {
            return null;
        }
        if (!reportName.endsWith(".jasper")) {
            reportName = reportName + ".jasper";
        }
        return getClass().getClassLoader().getResourceAsStream("rpt_templates/" + reportName);
    }

    /**
     * 该方法处理抛出的异常, 在子类中应该根据具体的异常进行处理
     * @param ex
     * @param data
     */
    public void handleException(Exception ex, Object data) {
        if (ex instanceof RuntimeException) {
            throw (RuntimeException) ex;
        } else {
            throw new RuntimeException(ex);
        }
    }
}
