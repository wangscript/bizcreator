/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.config.ObjectFactory;
import com.bizcreator.core.config.ObjectFactoryImpl;
import com.bizcreator.core.config.ObjectFactoryParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;

/**
 * 迷你小型容器，具备IOC的特点, 用于应用程序中对象的配置和管理:
 * 1. 从外部的XML重读取对象配置信息；
 * 2. 利用ObjectFactoryParser解析;
 * 3. 通过解析得到的元数据构造ObjectFactory;
 * 4. ObjectFactory可以利用简单的Hash表对对象进行管理，也可以通过PicoContainer进行管理
 * @author Administrator
 */
public class SysContext implements Serializable {

    private static Log log = LogFactory.getLog(SysContext.class);

    private final static String DEFAULT_CONFIG_RESOURCE = "com/rhinofield/base/config/default.rhinofield.cfg.xml";
    private final static String CONFIG_RESOURCE = "rhinofield.cfg.xml";

    private ObjectFactoryImpl objectFactory;
    ObjectFactoryParser objectFactoryParser;
    private URL configRes, defaultConfigRes;

    private static SysContext instance;

    //设计时的pico, 控件中引用pico, 可能在netbeans设计时使用
    private static MutablePicoContainer designTimePico;

    public static void main(String[] args) {
        String resource = null;
        if (args.length > 0) {
            resource = args[0];
            URL res = SysContext.class.getClassLoader().getResource(resource);
            if (res == null) {
                try {
                    res = new File(resource).toURL();
                } catch (MalformedURLException ex) {
                   ex.printStackTrace();
                }
            }
            if (res != null) {
                new SysContext(res).init();
            }
        }
        else {
            new SysContext().init();
        }
    }

    public SysContext() {
        this(null, null);
    }

    public SysContext(URL configRes) {
        this(configRes, null);
    }

    public SysContext(URL configRes, URL defaultConfigRes) {
        objectFactoryParser = new ObjectFactoryParser();
        objectFactory = new ObjectFactoryImpl();
        if (configRes != null) this.configRes = configRes;
        if (defaultConfigRes != null) this.defaultConfigRes = defaultConfigRes;
        instance = this;
    }

    public static SysContext get() {
        return instance;
    }

    public static MutablePicoContainer pico() {
        if (instance == null) {
            if (designTimePico == null) {
                designTimePico = new DefaultPicoContainer();
            }
            return designTimePico;
        }
        return instance.getObjectFactory().getPico();
    }

    public void init() {
        //create a PicoContainer
        DefaultPicoContainer pico = new DefaultPicoContainer();
        objectFactory.setPico(pico);

        //1. from default config file
        //Loading default config resource
        InputStream defaultIs = null;
        if (defaultConfigRes != null) {
            try {
                defaultIs = defaultConfigRes.openStream();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (defaultIs == null) {
            defaultIs = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_RESOURCE);
        }
        if (log.isDebugEnabled())  log.debug("default config resource: " + defaultIs);
        if (defaultIs != null) {
            objectFactoryParser.parseElementsStream(defaultIs, objectFactory);
        }

        //2. from custom config file
        InputStream customIs = null;
        if (configRes != null) {
            try {
                customIs = configRes.openStream();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (customIs == null) {

            customIs = getClass().getClassLoader().getResourceAsStream(CONFIG_RESOURCE);
        }
        if (customIs == null && defaultIs == null) {
            //todo
        }

        if (log.isDebugEnabled()) log.debug("custom config resource: " + customIs);
        if (customIs != null) {
            objectFactoryParser.parseElementsStream(customIs, objectFactory);
        }

        pico.start();
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public Object getObject(String name) {
        return pico().getComponent(name);
        //return objectFactory.createObject(name);
    }

    public void addComponent(Object key, Object obj) {
        pico().addComponent(key, obj);
    }

    public Object getComponent(Object componentKeyOrType) {
        return pico().getComponent(componentKeyOrType);
    }

    public <T> T getComponent(Class<T> componentType) {
        return pico().getComponent(componentType);
    }


}
