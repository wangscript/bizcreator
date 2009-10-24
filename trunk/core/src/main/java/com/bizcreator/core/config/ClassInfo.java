/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.config;

import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;

/**
 * class 元素主要用作参数, 不需要加入pico中, 因此不能作为一个component
 * @author Administrator
 */
public class ClassInfo extends AbstractObjectInfo {
    private static final long serialVersionUID = 1L;
    Class clazz = null;
    
    public ClassInfo(Element element, ObjectFactoryParser configParser) {
        super(element, configParser);
        try {
            String className = getValueString(element);
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public Object createObject(ObjectFactory objectFactory) {
        return clazz;
    }

    public void addToPico(MutablePicoContainer pico) {
        // do nothing
        //pico.addComponent(hasName() ? getName() : String.class, s);
    }
}