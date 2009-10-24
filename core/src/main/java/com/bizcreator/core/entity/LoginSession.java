/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 登录会话
 * @author lgh
 */
@Entity
@Table(name = "cbf_login_session")
public class LoginSession extends AtomicEntity implements Serializable {

    //用户名
    @FieldInfo(name="用户名")
    protected String loginName;
    
    //ip
    @FieldInfo(name="用户名")
    protected String ip;
    
    //登录时间
    @FieldInfo(name="登录时间")
    protected Date loginDate;
    
    //注销时间
    @FieldInfo(name="注销时间")
    protected Date logoutDate;
    
    //session id
    @FieldInfo(name="session id")
    protected String sessionId;

    @FieldInfo(name="活动会话")
    protected boolean living;
    
    @Override
	@Id @GeneratedValue(generator="domainIdGen")
	@GenericGenerator(name="domainIdGen", strategy="com.rhinofield.base.hibernate.id.DomainIdentifierGenerator",
		parameters={
			@Parameter(name="seq", value="cbf_login_session")
		}
	)
	@Column(name = "id", length = 30)
	public String getId() {
		return id;
	}
    
    @Column(name="login_name", length=100)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(name="ip", length=100)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name="login_date")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @Column(name="logout_date")
    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    @Column(name="session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name="is_living")
    public boolean isLiving() {
        return living;
    }

    public void setLiving(boolean living) {
        this.living = living;
    }
    
    
}