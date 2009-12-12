/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.impl;

import com.bizcreator.core.dao.BasicDao;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.orm.ibatis3.support.SqlSessionDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lgh
 */
@Transactional(readOnly = true)
public abstract class IBatis3Base extends SqlSessionDaoSupport implements BasicDao {

    public abstract String ns();

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AtomicEntity> T insert(T entity) {
        entity.setId(UUID.randomUUID().toString());
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            be.setActive(true);
            be.setCreated(new Date(System.currentTimeMillis()));
            be.setCreatedBy("Admin");
            be.setUpdated(new Date(System.currentTimeMillis()));
            be.setUpdatedBy("Admin");
            if (be.getClientId() == null) {
                be.setClientId("0");
            }
            if (be.getOrgId() == null) {
                be.setOrgId("0");
            }
        }
        getSqlSessionTemplate().insert(ns() + ".insert",entity);
        return entity;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AtomicEntity> T update(T entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            be.setUpdated(new Date(System.currentTimeMillis()));
            be.setUpdatedBy("Admin");
        }
        getSqlSessionTemplate().update(ns() + ".update", entity);
        return entity;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AtomicEntity> T save(T entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteById(Object id) {
        getSqlSessionTemplate().delete(ns() + ".deleteById", id);
    }

    public <T> T findById(Object id) {
        return (T)getSqlSessionTemplate().selectOne(ns() + ".findById", id);
    }

    public <T> List<T> listAll() {
        return (List<T>)getSqlSessionTemplate().selectList(ns() + ".listAll");
    }
}
