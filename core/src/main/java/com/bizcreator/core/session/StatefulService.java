/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session;

/**
 * 有状态的服务接口
 * @author lgh
 */
public interface StatefulService {
    
    public void create();
    
    public void activate();
    
    public void passivate();
    
    public void destroy();
    
    //------------------ 事务方法 --------------
    public void startTx();
    
    public void commit();
    
    public void rollback();
    
}
