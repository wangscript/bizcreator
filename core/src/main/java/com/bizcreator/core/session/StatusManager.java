/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session;

import com.bizcreator.core.session.ServiceBase;
import com.bizcreator.core.entity.CodeName;
import com.bizcreator.core.entity.StatusEntity;

/**
 * 状态管理
 * @author lgh
 */
public interface StatusManager extends ServiceBase {

    public final static String NAME = "statusManager";
    
    public void createStatusLog(StatusEntity master, CodeName op, CodeName status);
    
    public void createStatusLog(StatusEntity master, CodeName op, CodeName status, String desc);
}
