package com.bizcreator.core.session;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.dao.UserDao;

public interface UserManager extends UserDao {

	public final static String NAME = "core.userMgr";
	
	public LoginContext authenticate(String username, String password);
	
}
