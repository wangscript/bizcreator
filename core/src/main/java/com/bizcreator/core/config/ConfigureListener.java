/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.config;

import java.util.Map;

import com.bizcreator.core.ResourceManager;
import com.bizcreator.core.BizContext;
import com.bizcreator.core.session.impl.BizModelSession;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.web.context.ContextLoaderListener;

/**
 *
 * 在系统启动时配置应用环境
 * @author 罗冠华
 */
public class ConfigureListener extends ContextLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        
        ServletContext sc = event.getServletContext();
        ResourceManager.init(BizContext.class);
        BizContext.instance().init(sc);

        super.contextInitialized(event);

        System.out.println(">>>context initialized!!!");
        
        /*
        ServletContext sc = event.getServletContext();
        ResourceManager.init(RhinoCtx.class);
        RhinoCtx.instance().init(sc);
        //HibernateUtil.getSessionFactory(); // Just call the static initializer of that class
        ApplicationContext springContext = new ClassPathXmlApplicationContext("spring-context.xml");
        sc.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        Hibernate hibernate = (Hibernate)springContext.getBean("hibernate");
        RhinoCtx.instance().setHibernate(hibernate);
        RhinoCtx.instance().setSpringContext(springContext);
         */
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //HibernateUtil.getSessionFactory().close(); // Free all resources
        super.contextDestroyed(event);
    }
}
