/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.hibernate.Hibernate;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * FhinoFieldContext管理应用程序全局的状态信息
 * @author Administrator
 */
public class BizContext {

     //调用服务类型, 在web.xml的<init-param>中设置SERVICE_TYPE
    public static final String SERVICE_EJB3 = "ejb3";
    public static final String SERVICE_SEAM = "seam";
    public static final String SERVICE_HIBERNATE = "hibernate";
    public static final String SERVICE_IBATIS = "iBATIS";

    //默认服务类型
    public final static String DEFAULT_SERVICE_TYPE = SERVICE_EJB3;

    //服务类型参数
    public final static String SERVICE_PARAM_NAME = "SERVICE_TYPE";

    //Named query class
    public final static String NAMED_QUERIES_CLASS = "NAMED_QUERIES_CLASS";
    public final static String NAMED_QUERIES = "NAMED_QUERIES";
    
    //所属域
    public final static String DOMAIN = "DOMAIN";

    private ServletContext servletContext = null;
    private ApplicationContext springContext = null;

    private static BizContext instance = null;

    private ResourceManager resourceManager = null;

    protected BizContext() {
        //instance = this;
        this.resourceManager = ResourceManager.instance();
    }

    public BizContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.resourceManager = ResourceManager.instance();
    }

    public void init(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public static BizContext instance() {
        if (instance == null) {
            instance = new BizContext();
        }
        return instance;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public Object get(String name) {
        return servletContext.getAttribute(name);
    }

    public void set(String name, Object o) {
        servletContext.setAttribute(name, o);
    }

    /**
     * 获取指定的应用程序初始化参数的值
     * @param name
     * @return
     */
    public String getInitParameter(String name) {
        return servletContext.getInitParameter(name);
    }

    public ApplicationContext getSpringContext() {
        if (springContext == null) {
            springContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        }
        return springContext;
    }

    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    public static Object getBean(String name) {
        return instance().getSpringContext().getBean(name);
    }

    /**
     * 返回系统服务类型
     * @return
     */
    public String getServiceType() {
        return getInitParameter(SERVICE_PARAM_NAME);
    }

    public String getDomain() {
        return getInitParameter(DOMAIN);
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }
/*
    private ServiceLocator serviceLocator;
    public ServiceLocator getServiceLocator() {
        return serviceLocator;
    }

    public void setServiceLocator(ServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }
*/
    private Hibernate hibernate;
    public Hibernate getHibernate() {
        return hibernate;
    }

    public void setHibernate(Hibernate hibernate) {
        this.hibernate = hibernate;
    }

}
