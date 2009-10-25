/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.impl.BasicSession;
import com.bizcreator.core.entity.CodeName;
import com.bizcreator.core.entity.StatusEntity;
import com.bizcreator.core.entity.StatusLog;
import com.bizcreator.core.session.StatusManager;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author lgh
 */
@Transactional
public class StatusSession extends BasicSession implements StatusManager {

    public void createStatusLog(StatusEntity master, CodeName op, CodeName status) {
		createStatusLog(master, op, status, null);
	}

	public void createStatusLog(StatusEntity master, 
			CodeName op, CodeName status, String desc) {
		StatusLog statusLog = master.newStatus();
        
		statusLog.setMaster(master);
		statusLog.setOp(op);
		statusLog.setStatus(status);
		statusLog.setDescription(desc);
		statusLog.setHandleTime(new Date(System.currentTimeMillis()));
		statusLog.setHandler(getCtx().getUser().getUsername());
		persist(statusLog);
	}
}
