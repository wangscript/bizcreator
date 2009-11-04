package com.bizcreator.core.session.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.entity.User;
import com.bizcreator.core.session.UserManager;

public class UserSession extends IBatisBase implements UserManager {
	
	public final static String NS = "com.bizcreator.core.dao.UserDao";

	@Override
	public String ns() {
		return NS;
	}
	
	public LoginContext authenticate(String username, String password) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", password);
		
		SqlSession session = openSession();
        try {
            User user = (User) session.selectOne(ns() + ".authenticate", params);
            if (user != null) {
            	 LoginContext loginCtx = new LoginContext(user, user.getClientId(), user.getOrgId(), null);
                 loginCtx.setDomain("rhino");
                 return loginCtx;
            }
            
        } finally {
            session.close();
        }
        return null;
       
    }
	
}
