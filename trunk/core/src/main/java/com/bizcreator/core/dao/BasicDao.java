/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.dao;

/**
 *
 * @author lgh
 */
public interface BasicDao {

    public int insert(Object entity);

    public int update(Object entity);

    public int deleteById(String id);
    
}
