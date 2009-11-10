/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import java.io.Serializable;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.json.Jsonizable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.sf.json.JSONObject;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotEmpty;

/**
 *
 * @author lgh
 */
@Entity
@Table(name = "biz_user")
public class User extends BasicEntity implements com.bizcreator.core.security.User, Serializable, Jsonizable {

    @FieldInfo(name = "编码")
    protected String code;

    @FieldInfo(name = "EMail")
    protected String email;
    
    @FieldInfo(name = "座机")
    protected String phone;

    @FieldInfo(name = "手机")
    protected String mobile;

    @FieldInfo(name = "QQ")
    protected String qq;

    @FieldInfo(name = "MSN")
    protected String msn;

    @FieldInfo(name = "密码")
    protected String password;
    
    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "biz_user")
    })
    @Column(name = "id", length = 30)
    @Override
    public String getId() {
        return id;
    }

    @NotEmpty
    @Column(name = "name", length = 60)
    @Override
    public String getName() {
        return name;
    }

    @NotEmpty
    @Column(name = "code", length = 30)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "email", length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", length = 200)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "msn", length = 200)
    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    @Column(name = "qq", length = 200)
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    @Transient
    public String getUsername() {
        return name;
    }

	public Object fromJSON(JSONObject json) {
		
		super.fromJSON(json);
		
		this.code = json.getString("code");
		this.name = json.getString("name");
		this.email = json.getString("email");
		this.phone = json.getString("phone");
		this.mobile = json.getString("mobile");
		this.qq = json.getString("qq");
		this.msn = json.getString("msn");
		
		return this;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		
		json.put("code", this.code);
		json.put("name", this.name);
		json.put("email", this.email);
		json.put("phone", this.phone);
		json.put("mobile", this.mobile);
		json.put("qq", this.qq);
		json.put("msn", this.msn);
		
		return json;
	}

}