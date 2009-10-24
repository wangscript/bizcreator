/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session;

import javax.ejb.Stateful;

/**
 * 分批查询
 * @author lgh
 */
@Stateful
public interface BatchQuery extends StatefulService {

    public final static String NAME = "batchQuery";
    
    public void create(Class qeClass, Class fromClass, String extraFrom, 
            String filter, Object[] paramPairs);
    
    public void create(Class qeClass, Class fromClass, String filter, Object[] paramPairs);
    
    public void create(Class qeClass, Class fromClass, Object[] paramPairs);
    
    public void create(Class qeClass, Object[] paramPairs);
    
    public void setFields(String[] fields);
    
    public void execute();
    
    public ResultList current(int blockSize);
    
    public ResultList current();
    
    public ResultList first(int blockSize);
    
    public ResultList first();
    
    public ResultList previous(int blockSize);
    
    public ResultList previous();
    
    public ResultList next(int blockSize);
    
    public ResultList next();
    
    public ResultList last(int blockSize);
    
    public ResultList last();
    
    public int getCurrentBlockPosition();
    
    public int getTotalSize();
}
