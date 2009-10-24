/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 支持与类层次结构一样的Resource Map
 * @author Administrator
 */
public class InheritResourceManager extends ResourceManager {
    /**
     * 获取与类层次结构一样的Resource Map
     * @param clazz
     * @return
     */
    private ResourceMap getClassResourceMap(Class clazz) {
        String classResourceMapKey = clazz.getName();
        ResourceMap classResourceMap = resourceMaps.get(classResourceMapKey);
        if (classResourceMap == null) {
            List<Class> classes = new ArrayList<Class>();
            for(Class c = clazz; c != Object.class; c = c.getSuperclass()) {
                classes.add(c);
            }
            Collections.reverse(classes);

            ClassLoader classLoader = clazz.getClassLoader();
            ResourceMap appRM = getResourceMap();
            classResourceMap = createResourceMapChain(classLoader, appRM, classes);
            resourceMaps.put(classResourceMapKey, classResourceMap);
        }
        return classResourceMap;
    }

    private ResourceMap createResourceMapChain(ClassLoader classLoader, ResourceMap root, List<Class> classes) {
        ResourceMap parent = root;
        for (Class clazz : classes) {
            ResourceMap rm = resourceMaps.get(clazz.getName());
            if (rm == null) {
                List<String> bundleNames = getClassBundleNames(clazz);
                rm = new ResourceMap(parent, classLoader, bundleNames);
                resourceMaps.put(clazz.getName(), rm);
            }
            parent = rm;
        }
        return parent;
    }

    public ResourceMap getResourceMap(Class startClass, Class stopClass) {
        throw new UnsupportedOperationException("继承型资源管理器不支持该操作!");
    }

    public ResourceMap getResourceMap(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null class");
        }
        return getClassResourceMap(cls);
    }

    
}
