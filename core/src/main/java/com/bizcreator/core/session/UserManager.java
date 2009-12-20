package com.bizcreator.core.session;

import com.bizcreator.core.LoginContext;
import java.util.List;

public interface UserManager extends ServiceBase {

	public final static String NAME = "core.userMgr";

        public void deleteById(Object id);

        public <T> T findById(Object id);

        public <T> List<T> listAll();

	public LoginContext authenticate(String username, String password);
	
}
