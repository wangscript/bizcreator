/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import java.io.Serializable;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.json.BizJsonObject;
import com.bizcreator.core.json.Jsonizable;
import com.google.gson.JsonObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;



import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotEmpty;

/**
 *
 * @author lgh
 */
//@Entity
//@Table(name = "biz_user")
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

	public Object fromJson(BizJsonObject json) {
		
		super.fromJson(json);
		
		this.code = json.getAsString("code");
		this.name = json.getAsString("name");
		this.email = json.getAsString("email");
		this.phone = json.getAsString("phone");
		this.mobile = json.getAsString("mobile");
		this.qq = json.getAsString("qq");
		this.msn = json.getAsString("msn");
		
		return this;
	}

	public JsonObject toJson() {
		JsonObject json = super.toJson();
		
		json.addProperty("code", this.code);
		json.addProperty("name", this.name);
		json.addProperty("email", this.email);
		json.addProperty("phone", this.phone);
		json.addProperty("mobile", this.mobile);
		json.addProperty("qq", this.qq);
		json.addProperty("msn", this.msn);
		
		return json;
	}

}
