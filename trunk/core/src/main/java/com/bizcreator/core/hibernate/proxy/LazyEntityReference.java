/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy;
import com.bizcreator.core.session.LazyLoading;
import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.hibernate.proxy.client.ProxyFactory;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import org.hibernate.HibernateException;

/**
 * Uninitialized proxy representation
 * @author Juozas
 *
 */
public class LazyEntityReference implements Serializable {

    private static final long serialVersionUID = 7799616869249915673L;

    private Serializable id;
    private int token;
    private String className;
    
    public LazyEntityReference(){}
    
    public LazyEntityReference(int token,String className,Serializable id){
        this.token = token;
        this.className = className;
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Serializable getId() {
        return id;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    Object readResolve() throws ObjectStreamException, ClassNotFoundException, 
            RemoteException, HibernateException, NamingException, Exception {
        
        //return ProxyFactory.create(this);
        
        
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            LazyLoading lazyLoading =  (LazyLoading) ServiceFactory.getService(LazyLoading.NAME);
            return lazyLoading.initializeEntity(className, id, token);
        }
        else {
            return ProxyFactory.create(this);
        }
    }
    
}
