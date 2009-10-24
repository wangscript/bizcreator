/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.config;

import com.bizcreator.core.BizContext;
import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.entity.LoginSession;
import com.bizcreator.core.session.ServiceBase;
import java.util.Date;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.hibernate.Session;


/**
 *
 * @author lgh
 */
public class SessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent e) {
        System.out.println(">>>session created: " + e.getSession().getId());
    }

    public void sessionDestroyed(HttpSessionEvent e) {
        System.out.println(">>>session destroyed: " + e.getSession().getId());
        Session hibernateSession = BizContext.instance().getHibernate().getSession();
        try {
            ServiceBase serviceBase = (ServiceBase) ServiceFactory.getService(ServiceBase.NAME);
            LoginSession loginSession = (LoginSession) e.getSession().getAttribute("loginSession");
            loginSession.setLogoutDate(new Date(e.getSession().getLastAccessedTime()));
            loginSession.setLiving(false);
            hibernateSession.beginTransaction();
            serviceBase.save(loginSession);
            hibernateSession.getTransaction().commit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            hibernateSession.getTransaction().rollback();
        }
        finally {
            hibernateSession.close();
        }
    }

}
