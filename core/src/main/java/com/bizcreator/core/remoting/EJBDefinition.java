package com.bizcreator.core.remoting;

import java.io.Serializable;

/**
 * EJB定义：
 * 
 * @author luoguanhua
 * @version 1.0.0
 */
public class EJBDefinition extends ServiceDefinition implements Serializable{
	//
    private String home;
    private String remote;
    private String localHome;
    private String local;
    
    
    public EJBDefinition(String serviceName, String remoteClassName) {
    	this(serviceName, null, remoteClassName);
    }
    
    public EJBDefinition(String serviceName, String homeClassName,
                         String remoteClassName) {
    	super(serviceName, null);
        this.home = homeClassName;
        this.remote = remoteClassName;
    }

    public EJBDefinition(String serviceName,
                         String homeClassName, String remoteClassName,
                         String localHomeClassName, String localClassName) {
        super(serviceName, null);
        this.home = homeClassName;
        this.remote = remoteClassName;
        this.localHome = localHomeClassName;
        this.local = localClassName;
    }
    
    public Class getHomeClass() {
        try {
            return Class.forName(home);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Unable to load the class : " + home);
        }
    }

    public Class getRemoteClass() {
        try {
            return Class.forName(remote);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Unable to load the class : " + remote);
        }
    }

    public String getRemoteName() {
        return remote;
    }

    public String getHomeName() {
        return home;
    }

    public boolean equals(Object obj) {
        if (obj instanceof EJBDefinition) {
            EJBDefinition definition = (EJBDefinition) obj;
            if (!(definition.getHomeName().equals(home))
                    || !(definition.getServiceName().equals(this.getServiceName()))
                    || !(definition.getRemoteName().equals(remote)))
                return false;
            return true;
        }
        return false;
    }

}
