package com.bizcreator.core;

import com.bizcreator.core.security.User;
import javax.servlet.http.HttpSession;

public interface RhSessionContext //extends SessionContext 
{
	
	public final static String CLIENT_ID = "clientId";
	public final static String ORG_ID = "orgId";
	
    //public final static String USER = "user";
	//public final static String USERNAME = "username";
	//public final static String PASSWORD = "password";
	
	public final static String DUTY_ID = "dutyId";
	public final static String RESPONSIBILITY = "responsibility";
	
	public HttpSession getSession();
	
	public String getDutyId();
	
	//public Responsibility getResponsibility(String menuId);

	public String  getClientId();
	
	public String getOrgId();
    
    public User getUser();
	
    /**
    * Get a value by name.
    */
   public Object get(String name);
   
   /**
    * Set a value.
    */
   public void set(String name, Object value);
}
