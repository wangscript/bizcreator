/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.security;

import com.bizcreator.core.LoginContext;

/**
 *
 * @author lgh
 */
public interface Authenticate {

    public final static String NAME = "personManager";
    
    public LoginContext authenticate(String userName, String password);

}
