package com.bizcreator.core.session.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bizcreator.core.dao.BasicDao;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.ibatis.Ibatis3DaoSupport;

@Transactional(readOnly = true)
public abstract class IBatisBase extends Ibatis3DaoSupport implements BasicDao {
	
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
			if (be.getClientId() == null)
				be.setClientId("0");
			if (be.getOrgId() == null)
				be.setOrgId("0");
		}


		SqlSession session = openSession();
        session.insert(ns() + ".insert", entity);
        return entity;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public <T extends AtomicEntity> T update(T entity) {
		if (entity instanceof BasicEntity) {
			BasicEntity be = (BasicEntity) entity;
			be.setUpdated(new Date(System.currentTimeMillis()));
			be.setUpdatedBy("Admin");
		}
		SqlSession session = openSession();
        session.update(ns() + ".update", entity);
        return entity;
        
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public <T extends AtomicEntity> T save(T entity) {
		if (entity.getId() == null) {
			return insert(entity);
		}
		else {
			return update(entity);
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteById(Object id) {
		SqlSession session = openSession();
        session.delete(ns() + ".deleteById", id);
	}

	public <T> T findById(Object id) {
		SqlSession session = openSession();
        T entity = (T) session.selectOne(ns() + ".findById", id);
        return entity;
	}

	public <T> List<T> listAll() {
		SqlSession session = openSession();
        List<T> list = (List<T>) session.selectList(ns() + ".listAll");
        return list;
	}
}
