package com.bizcreator.core.session;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.dao.BasicDao;

public interface UserManager extends BasicDao {

	public final static String NAME = "core.userMgr";
	
	public LoginContext authenticate(String username, String password);
	
}
