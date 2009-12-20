/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.dao.UserDao;

/**
 *
 * @author lgh
 */
public interface IbatisUserMgr extends UserDao {

	public final static String NAME = "core.userMgr";

	public LoginContext authenticate(String username, String password);
}
