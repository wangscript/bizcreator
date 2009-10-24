package com.bizcreator.core;

import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.security.User;

import javax.ejb.SessionContext;
import javax.servlet.http.HttpSession;

public class SessionContextImpl implements RhSessionContext {

    private SessionContext ctx;
    public final static ThreadLocal<HttpSession> session = new ThreadLocal<HttpSession>();

    public SessionContextImpl(SessionContext ctx) {
        this.ctx = ctx;
    }

    public HttpSession getSession() {
        return session.get();
    }

    public Object get(String name) {
        return getSession().getAttribute(name);
    }

    public void set(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    public String getDutyId() {
        String dutyId = (String) getSession().getAttribute(DUTY_ID);
        if (dutyId == null) {
            throw new RuntimeException("Duty has not assigned for the user.");
        }
        return dutyId;
    }

    /*
    public Responsibility getResponsibility(String menuId) {
    String dutyId = getDutyId();
    try {
    OrgManager orgManager = (OrgManager) ServiceFactory.getSerivice(CommonServices.ORG_MANAGER);
    Responsibility responsibility = orgManager.findResponsibility(dutyId, menuId);
    return responsibility;
    }
    catch (NamingException ex) {
    ex.printStackTrace();
    return null;
    }
    }
     */
    /**
     * 返回登录asp客户id
     * @return
     */
    public String getClientId() {
        String id = (String) getSession().getAttribute(CLIENT_ID);
        if (id == null) {
            throw new RuntimeException("Client has not assigned for the user.");
        }
        return id;

        //return "000000000015@rhino";
    }

    public String getOrgId() {
        String id = (String) getSession().getAttribute(ORG_ID);
        if (id == null) {
            throw new RuntimeException("Org has not assigned for the user.");
        }
        return id;

        //return "000000000016@rhino";
    }

    public User getUser() {
        User user = (User) getSession().getAttribute(User.USER_KEY);
        if (user == null) {
            throw new RuntimeException("You hav not login yet!");
        }
        return user;

        //return "lgh";
        //return ctx.getCallerPrincipal().getName();
    }

    /*
    public Principal getCallerPrincipal() {
    return ctx.getCallerPrincipal();
    }

    public boolean isCallerInRole(Identity arg0) {
    return ctx.isCallerInRole(arg0);
    }

    public boolean isCallerInRole(String arg0) {
    return ctx.isCallerInRole(arg0);
    }

    public Object getBusinessObject(Class arg0) throws IllegalStateException {
    return ctx.getBusinessObject(arg0);
    }

    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
    return ctx.getEJBLocalObject();
    }

    public EJBObject getEJBObject() throws IllegalStateException {
    return ctx.getEJBObject();
    }

    public Class getInvokedBusinessInterface() throws IllegalStateException {
    return ctx.getInvokedBusinessInterface();
    }

    public MessageContext getMessageContext() throws IllegalStateException {
    return ctx.getMessageContext();
    }

    public Identity getCallerIdentity() {
    return ctx.getCallerIdentity();
    }

    public EJBHome getEJBHome() {
    return ctx.getEJBHome();
    }

    public EJBLocalHome getEJBLocalHome() {
    return ctx.getEJBLocalHome();
    }

    public Properties getEnvironment() {
    return ctx.getEnvironment();
    }

    public boolean getRollbackOnly() throws IllegalStateException {
    return ctx.getRollbackOnly();
    }

    public void setRollbackOnly() throws IllegalStateException {
    ctx.setRollbackOnly();
    }

    public TimerService getTimerService() throws IllegalStateException {
    return ctx.getTimerService();
    }

    public UserTransaction getUserTransaction() throws IllegalStateException {
    return ctx.getUserTransaction();
    }

    public Object lookup(String arg0) {
    return ctx.lookup(arg0);
    }
     */
}
