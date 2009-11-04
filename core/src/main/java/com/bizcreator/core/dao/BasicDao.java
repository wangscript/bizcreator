/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.dao;

import java.util.List;

import com.bizcreator.core.entity.AtomicEntity;

/**
 *
 * @author lgh
 */
public interface BasicDao {

	public <T extends AtomicEntity> T insert(T entity);

    public <T extends AtomicEntity> T update(T entity);

    public void deleteById(Object id);
    
    public <T> T findById(Object id);
    
    public <T> List<T> listAll(); 
    
}
