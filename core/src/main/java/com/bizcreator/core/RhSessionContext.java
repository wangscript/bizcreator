package com.bizcreator.core;

import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.security.User;
import javax.servlet.http.HttpSession;

public interface RhSessionContext //extends SessionContext 
{
	
	public final static String CLIENT_ID = "client_id";
	public final static String ORG_ID = "org_id";
	
    //public final static String USER = "user";
	//public final static String USERNAME = "username";
	//public final static String PASSWORD = "password";
	
	public final static String DUTY_ID = "dutyId";
	public final static String RESPONSIBILITY = "responsibility";

        public final static String USER_ORG = "userOrg";
        public final static String USER_PERSON = "userPerson";
        
	public HttpSession getSession();
	
	public String getDutyId();
	
	//public Responsibility getResponsibility(String menuId);

	public String  getClient_id();
	
	public String getOrg_id();

        public AtomicEntity getUserOrg();

        public AtomicEntity getUserPerson();
        
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
