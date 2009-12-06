/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.json.JSONConverter;
import com.bizcreator.core.json.Jsonizable;
import com.bizcreator.core.security.User;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * 维护登录的环境信息
 * @author Administrator
 */
public class LoginContext implements java.io.Serializable, Jsonizable {

    private String domain;

    private User user;
    private String clientId;
    private String orgId;
    private String dutyId;

    private Map<String, Object> attributes = new HashMap<String, Object> ();

    public LoginContext(User user, String clientId,
            String orgId,  String dutyId) {
        this.user = user;
        this.clientId = clientId;
        this.orgId = orgId;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    public JSONObject toJSON() {
        return JSONConverter.toJSON(this, new String[]{"domain", "clientId", "orgId", "dutyId"});
    }

    public Object fromJSON(JSONObject json) {
        return null;
    }

}
