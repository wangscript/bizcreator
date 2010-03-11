package com.bizcreator.core.session.impl;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.entity.User;
import com.bizcreator.core.session.UserManager;

@Transactional(readOnly = true)
public class UserSession extends BasicSession implements UserManager {

    public LoginContext authenticate(String username, String password) {
        User user = (User) getSingleResult(" from com.bizcreator.core.entity.User o  where o.name = ? and o.password = ? ",
                new Object[]{username, password}, false);
        if (user == null) {
            return null;
            //throw new BaseException("登录错误，请输入正确的用户名和密码!");
        }
        LoginContext loginCtx = new LoginContext(user, user.getClient_id(), user.getOrg_id(), null);
        loginCtx.setDomain(getDomain());
        return loginCtx;
    }

    public void deleteById(Object id) {
        remove(User.class, id);
    }

    public <T> T findById(Object id) {
        return (T) find(User.class, id);
    }

    public <T> List<T> listAll() {

        List<T> all = executeQuery("from com.bizcreator.core.entity.User o order by o.name");
        return all;

    }
}
