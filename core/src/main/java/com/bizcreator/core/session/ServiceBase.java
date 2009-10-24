package com.bizcreator.core.session;

import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.EntryEntity;
import java.util.Collection;
import java.util.List;

import java.util.Map;
import javax.ejb.Local;
import org.hibernate.ScrollableResults;

/**
 * 基本的服务操作类, 其他的业务管理类都从该类继承
 * @author Administrator
 *
 */
@Local
public interface ServiceBase {

    public final static String NAME = "serviceBase";

    public RhSessionContext getCtx();

    public void persist(Object entity);

    public <T> T merge(T entity);

    public <T extends AtomicEntity> T create(T entity);

    public <T extends AtomicEntity> T update(T entity);

    public <T extends AtomicEntity> T save(T entity);

    public <T extends AtomicEntity> void create(T[] entities);

    public <T extends AtomicEntity> void update(T[] entities);

    public <T extends AtomicEntity> T save(T master, Collection<? extends EntryEntity> ... entries);

    public void remove(AtomicEntity entity);

    public void removeFromClient(AtomicEntity entity);

    public void remove(Class entityClass, Object primaryKey);

    public <T extends AtomicEntity> T[] save(T[] entities);

    public void remove(Class entityClass, Object[] ids);

    public <A> A find(Class<A> entityClass, Object primaryKey);

    public <A> A findByCode(Class<A> clazz, String code);

    public <A> A findByName(Class<A> clazz, String name);

    //查询列表
    public List executeQuery(String qlString);

    public List executeQuery(String qlString, Object[] params);

    public List executeQuery(String qlString, Map<String, Object> paramMap);

    public List executeNpQuery(String qlString, Object[] paramPairs);

    //查询单行对象
    public Object getSingleResult(String qlString);

    public Object getSingleResult(String qlString, Object[] params, boolean filterByClients);

    public Object getSingleResult(String qlString, Object[] params);

    public Object getSingleResult(String qlString, Map<String, Object> paramMap);

    public Object getNpSingleResult(String qlString, Object[] paramPairs, boolean filterByClients);

    public Object getNpSingleResult(String qlString, Object[] paramPairs);

    //执行更新
    public int executeUpdate(String qlString);

    public int executeUpdate(String qlString, Object[] params);

    public int executeUpdate(String qlString, Map<String, Object> paramMap);

    public int executeNpUpdate(String qlString, Object[] paramPairs);

    //scroll 查询
    public ScrollableResults scroll(String qlString);

    public ScrollableResults scroll(String qlString, Object[] params);

    public ScrollableResults scroll(String qlString, Map<String, Object> paramMap);

    public ScrollableResults scrollNp(String qlString, Object[] paramPairs);

    public void setFetchSize(int fetchSize);

    //给定select语句，通过fromClass和相应的条件、参数进行查询
    public List executeQuery(String qlSelect, Class fromClass, String filter, Object[] paramPairs);

    public List executeQuery(String qlSelect, Class fromClass, Object[] paramPairs);

    //根据query和filter查询
    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String extraFrom, String filter, Object[] paramPairs);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String extraFrom, String filter, Map paramMap);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String filter, Object[] paramPairs);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, String filter, Map paramMap);

    public <A> List<A> queryEntities(Class<A> qeClass, String filter, Object[] paramPairs);

    public <A> List<A> queryEntities(Class<A> qeClass, String filter, Map paramMap);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, Object[] paramPairs);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass, Map paramMap);

    public <A> List<A> queryEntities(Class<A> qeClass, Object[] paramPairs);

    public <A> List<A> queryEntities(Class<A> qeClass, Map paramMap);

    public <A> List<A> queryEntities(Class<A> qeClass, Class fromClass);

    //--------------------------- 按字段查询数据 ---------------------------
    public List queryDataList(String[] fields, Class qeClass, Class fromClass, String extraFrom,
            String filter, Object[] paramPairs);

    public List queryDataList(String[] fields, Class qeClass, Class fromClass, String filter,
            Object[] paramPairs);

    public List queryDataList(String[] fields, Class fromClass, String filter, Object[] paramPairs);

    //用于RhLookupField
    public Object[] searchEntities(Class qeClass, Class fromClass, String param, Object[] paramPairs);

    public Object[] searchEntities(Class qeClass, Class fromClass, String param);

    //-------------- atomics
    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass, String filter, Object[] paramPairs);

    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass, Object[] paramPairs);

    public List<AtomicEntity> queryAtomics(Class qeClass, Object[] paramPairs);

    public List<AtomicEntity> queryAtomics(Class qeClass, Class fromClass);

    //-------------- basic entities
    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass, String filter, Object[] paramPairs);

    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass, Object[] paramPairs);

    public List<BasicEntity> queryBasicEntities(Class qeClass, Object[] paramPairs);

    public List<BasicEntity> queryBasicEntities(Class qeClass, Class fromClass);

    //public String login(String name, String password);
    public void handleException(Exception ex, Object data);
}
