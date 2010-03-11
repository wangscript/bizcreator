/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.json.BizJsonObject;
import com.bizcreator.core.json.Jsonizable;
import com.bizcreator.core.security.User;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 维护登录的环境信息
 * @author Administrator
 */
public class LoginContext implements java.io.Serializable, Jsonizable {

    private String domain;

    private User user;
    private String client_id;
    private String org_id;
    private String dutyId;

    private Map<String, Object> attributes = new HashMap<String, Object> ();

    public LoginContext(User user, String client_id,
            String org_id,  String dutyId) {
        this.user = user;
        this.client_id = client_id;
        this.org_id = org_id;
        this.dutyId = dutyId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
            return attributes.get(key);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public JsonObject toJson() {
    	JsonObject json = new JsonObject();
    	json.addProperty("domain", this.domain);
    	json.addProperty("client_id", this.client_id);
    	json.addProperty("org_id", this.org_id);
    	json.addProperty("dutyId", this.dutyId);
    	return json;
    }

    public Object fromJson(BizJsonObject json) {
    	
    	this.domain = json.getAsString("domain");
    	this.client_id = json.getAsString("client_id");
    	this.org_id = json.getAsString("org_id");
    	this.dutyId = json.getAsString("dutyId");
    	
        return this;
    }

}
