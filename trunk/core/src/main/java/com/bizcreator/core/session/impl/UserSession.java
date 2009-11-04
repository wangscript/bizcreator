package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.UserManager;

public class UserSession extends IBatisBase implements UserManager {
	
	public final static String NS = "com.bizcreator.core.dao.UserDao";

	@Override
	public String ns() {
		return NS;
	}
	
}
