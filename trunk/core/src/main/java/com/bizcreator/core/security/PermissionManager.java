/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.security;

/**
 *
 * @author lgh
 */
public interface PermissionManager {

    public final static String NAME = "responsibilityManager";
    
    //职位模块权限
	public ResponsibilityPerm findResponsibility(String dutyId, String menuId);

	public ResponsibilityPerm findResponsibility(String menuId);
    
}
