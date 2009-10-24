/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy;

import com.bizcreator.core.hibernate.proxy.client.ProxyFactory;
import java.io.ObjectStreamException;
import java.io.Serializable;


public class LazyCollectionReference implements Serializable{
    
    private static final long serialVersionUID = 183925776415464990L;
    private Serializable id;
    private String role;
    private String collectionType;
    
    public Serializable getId() {
        return id;
    }
    public void setId(Serializable id) {
        this.id = id;
    }
    public String getRole() {
        
        return role;
    }
    
    public void setRole(String role){
        this.role = role;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
    
    Object readResolve() throws ObjectStreamException{
        return ProxyFactory.create(this);
    }
    
}

