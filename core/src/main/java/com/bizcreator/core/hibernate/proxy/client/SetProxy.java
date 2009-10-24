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
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Administrator
 */
public class SetProxy extends AbstractSet implements Serializable{//

    private final LazyCollectionReference reference;
    private Set remote;

    public SetProxy(LazyCollectionReference reference) {
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

    private Set load() {
        try {
            if (remote == null) {
                //remote = (Set) ClientSession.getLoader().initializeCollection(reference.getRole(),reference.getId());
                
                //将该ListProxy传到服务器端时, 应直接调用LazyLoadingSession.initializeCollection
                String endPoint = (String) System.getProperties().get("end_point");
                System.out.println(">>> end point: " + endPoint);
                if ("server".equals(endPoint)) {
                    //LazyLoading lazyLoading = (LazyLoading) BeanFactory.get().getBean(LazyLoading.NAME);
                    LazyLoading lazyLoading = (LazyLoading) ServiceFactory.getService(LazyLoading.NAME);
                    remote = (Set) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
                else {
                    LazyLoading lazyLoading = (LazyLoading) SysContext.pico().getComponent(LazyLoading.NAME);
                    remote = (Set) lazyLoading.initializeCollection(reference.getRole(), reference.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return remote;
    }

    public int size() {
        return load().size();
    }

    public boolean isEmpty() {
        return load().isEmpty();
    }

    public Object[] toArray() {
        return load().toArray();
    }

    public boolean contains(Object o) {
        return load().contains(o);
    }

    public boolean containsAll(Collection c) {
        return load().containsAll(c);
    }

    public Iterator iterator() {
        return load().iterator();
    }

    public Object[] toArray(Object[] a) {

        return load().toArray(a);
    }
}
