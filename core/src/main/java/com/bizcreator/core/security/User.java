/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.security;

import java.io.Serializable;

/**
 *
 * @author lgh
 */
public interface User extends Serializable {

    public final static String USER_KEY = "User";
    
    public String getId();
    
    public String getUsername();
    
    public String getPassword();
}
