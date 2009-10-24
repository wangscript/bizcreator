/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy.client;

import com.bizcreator.core.ServiceFactory;
import com.bizcreator.core.SysContext;
import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import com.bizcreator.core.session.LazyLoading;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class MapProxy extends AbstractMap implements Serializable  {//
    
    private final LazyCollectionReference reference;
    private Map remote;
    
    public MapProxy(LazyCollectionReference reference) {
        super();
        this.reference = reference;
    }

    Object writeReplace() throws ObjectStreamException {
        if (remote != null) {
            return remote;
        } else {
            return reference;
        }
    }
    
    private Map load() {
        try {
            if (remote == null) {
                //remote = (Set) ClientSession.getLoader().initializeCollection(reference.getRole(),reference.getId());
                
                //将该ListProxy传到服务器端时, 应直接调用LazyLoadingSession.initializeCollection
                String endPoint = (String) System.getProperties().get("end_point");
                if ("server".equals(endPoint)) {
                    //LazyLoading lazyLoading = (LazyLoading) BeanFactory.get().getBean(LazyLoading.NAME);
                    LazyLoading lazyLoading = (LazyLoading) ServiceFactory.getService(LazyLoading.NAME);
                    remote = (Map) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
                else {
                    LazyLoading lazyLoading = (LazyLoading) SysContext.pico().getComponent(LazyLoading.NAME);
                    remote = (Map) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remote;
    }
    
    @Override
    public Set entrySet() {
        return load().entrySet();
    }

}