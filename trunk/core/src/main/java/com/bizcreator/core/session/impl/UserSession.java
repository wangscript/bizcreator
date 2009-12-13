package com.bizcreator.core.session.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.dao.UserDao;
import com.bizcreator.core.entity.User;
import com.bizcreator.core.session.UserManager;

@Transactional(readOnly = true)
public class UserSession extends IBatis3Base implements UserManager {

    public final static String NS = UserDao.class.getName();

    public String ns() {
        return NS;
    }

    public LoginContext authenticate(String username, String password) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);

        //SqlSession session = openSession();
        //User user = (User) session.selectOne(ns() + ".authenticate", params);
        User user = (User) getSqlSessionTemplate().selectOne(ns() + ".authenticate", params);
        if (user != null) {
            LoginContext loginCtx = new LoginContext(user, user.getClientId(), user.getOrgId(), null);
            loginCtx.setDomain("rhino");
            return loginCtx;
        }

        return null;

    }
}
