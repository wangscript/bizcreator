/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.hibernate.proxy.client;

import com.bizcreator.core.hibernate.proxy.LazyCollectionReference;
import com.bizcreator.core.hibernate.proxy.LazyEntityReference;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
        
/**
 *
 * @author Administrator
 */
public class ProxyFactory {//
    
    public static Object create(LazyEntityReference ref) throws ClassNotFoundException {
        return EntityProxy.create(ref);
    }
    
    public static Object create(LazyCollectionReference ref) {
        
        String collectionType = ref.getCollectionType();
        if ("set".equals(collectionType)) {
            Set set = new SetProxy(ref);
            return Collections.unmodifiableSet(set);
        }
        else if ("bag".equals(collectionType) || "list".equals(collectionType)) {
            List list = new ListProxy(ref);
            return list;
            //return Collections.unmodifiableList(list);
        }
        else if ("map".equals(collectionType)) {
            Map map = new MapProxy(ref);
            return map;
        }
        else {
            throw new RuntimeException("Sorry, Not support this collection type yet: " + collectionType + "!");
        }
    }
}