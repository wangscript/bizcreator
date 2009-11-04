package com.bizcreator.core.session.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;

import com.bizcreator.core.dao.BasicDao;
import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.ibatis.Ibatis3DaoSupport;

public abstract class IBatisBase extends Ibatis3DaoSupport implements BasicDao {
	
	public abstract String ns();
	
	public <T extends AtomicEntity> T insert(T entity) {
		
		entity.setId(UUID.randomUUID().toString());
		if (entity instanceof BasicEntity) {
			BasicEntity be = (BasicEntity) entity;
			be.setActive(true);
			be.setCreated(new Date(System.currentTimeMillis()));
			be.setCreatedBy("Admin");
		}
		SqlSession session = openSession();
        try {
            session.insert(ns() + ".insert", entity);
            return entity;
        } finally {
            session.close();
        }
	}
	
	public <T extends AtomicEntity> T update(T entity) {
		if (entity instanceof BasicEntity) {
			BasicEntity be = (BasicEntity) entity;
			be.setUpdated(new Date(System.currentTimeMillis()));
			be.setUpdatedBy("Admin");
		}
		SqlSession session = openSession();
        try {
            session.insert(ns() + ".update", entity);
            return entity;
        } finally {
            session.close();
        }
	}
	
	public void deleteById(Object id) {
		SqlSession session = openSession();
        try {
            session.insert(ns() + ".deleteById", id);
        } finally {
            session.close();
        }
		
	}

	public <T> T findById(Object id) {
		SqlSession session = openSession();
        try {
            T entity = (T) session.selectOne(ns() + ".findById", id);
            return entity;
        } finally {
            session.close();
        }
	}

	

	public <T> List<T> listAll() {
		SqlSession session = openSession();
        try {
            List<T> list = (List<T>) session.selectList(ns() + ".listAll");
            return list;
        } finally {
            session.close();
        }
	}
}
