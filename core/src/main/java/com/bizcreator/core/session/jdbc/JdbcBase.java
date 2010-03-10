package com.bizcreator.core.session.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.BizEntity;

@Transactional(readOnly = true)
public class JdbcBase extends JdbcDaoSupport  {

	public BizEntity getBizEntity(String code) {
		
		BizEntity be = (BizEntity) this.getJdbcTemplate().queryForObject(  
			"SELECT * FROM biz_entity WHERE code = ?",  new Object[]{code},   
			new RowMapper<BizEntity>() {
				public BizEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
					BizEntity be = new BizEntity();
					be.fromRs(rs);
					return be;
				}
			}
		);
		return be;
		
	}
	
	//1. 新增
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
        
        return entity;
	}
	
	//2. 修改
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AtomicEntity> T update(T entity) {
        if (entity instanceof BasicEntity) {
            BasicEntity be = (BasicEntity) entity;
            be.setUpdated(new Date(System.currentTimeMillis()));
            be.setUpdatedBy("Admin");
        }
        //getSqlSessionTemplate().update(ns() + ".update", entity);
        return entity;
    }
	
	//3. 保存
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public <T extends AtomicEntity> T save(T entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }
	
	//4. 删除
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteById(Object id) {
        //getSqlSessionTemplate().delete(ns() + ".deleteById", id);
    }

	//5. 按主键查找
    public <T> T findById(Object id) {
    	return null;
        //return (T)getSqlSessionTemplate().selectOne(ns() + ".findById", id);
    }

    //查找全部
    public <T> List<T> listAll() {
    	return null;
        //return (List<T>)getSqlSessionTemplate().selectList(ns() + ".listAll");
    }
    
	
	
}
