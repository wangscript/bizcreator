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
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ListProxy extends AbstractList implements Serializable {
    
    private final LazyCollectionReference reference;
    private List remote;

    public ListProxy(LazyCollectionReference reference) {
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

    private List load() {
        try {
            if (remote == null) {
                //remote = (Set) ClientSession.getLoader().initializeCollection(reference.getRole(),reference.getId());
                
                //将该ListProxy传到服务器端时, 应直接调用LazyLoadingSession.initializeCollection
                String endPoint = (String) System.getProperties().get("end_point");
                if ("server".equals(endPoint)) {
                    //LazyLoading lazyLoading = (LazyLoading) BeanFactory.get().getBean(LazyLoading.NAME);
                    LazyLoading lazyLoading = (LazyLoading)ServiceFactory.getService(LazyLoading.NAME);
                    remote = (List) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
                else {
                    LazyLoading lazyLoading = (LazyLoading) SysContext.pico().getComponent(LazyLoading.NAME);
                    remote = (List) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remote;
    }
    
    @Override
    public boolean add(Object o) {
        return load().add(o);
    }
    
    @Override
    public boolean remove(Object o) {
        return load().remove(o);
    }
    
    @Override
    public Object get(int index) {
        return load().get(index);
    }
    
    @Override
    public int size() {
        return load().size();
    }

    @Override
    public boolean isEmpty() {
        return load().isEmpty();
    }
    
    @Override
    public Object[] toArray() {
        return load().toArray();
    }
    
    @Override
    public boolean contains(Object o) {
        return load().contains(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return load().containsAll(c);
    }
    
    @Override
    public Iterator iterator() {
        return load().iterator();
    }
    
    @Override
    public Object[] toArray(Object[] a) {
        return load().toArray(a);
    }
}