/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.bizcreator.core.config;

import com.bizcreator.util.XmlUtil;
import java.util.ArrayList;
import java.util.List;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;

public class ListInfo extends AbstractObjectInfo {

    private static final long serialVersionUID = 1L;
    ObjectInfo[] elementInfos = null;

    public ListInfo(Element listElement, ObjectFactoryParser configParser) {
        super(listElement, configParser);

        List elementElements = XmlUtil.elements(listElement);
        elementInfos = new ObjectInfo[elementElements.size()];
        for (int i = 0; i < elementElements.size(); i++) {
            Element elementElement = (Element) elementElements.get(i);
            elementInfos[i] = configParser.parse(elementElement);
        }
    }

    public Object createObject(ObjectFactory objectFactory) {
        List list = new ArrayList();
        if (elementInfos != null) {
            for (int i = 0; i < elementInfos.length; i++) {
                Object element = objectFactory.getObject(elementInfos[i]);
                list.add(element);
            }
        }
        return list;
    }

    public void addToPico(MutablePicoContainer pico) {
       List list = new ArrayList();
        if (elementInfos != null) {
            for (int i = 0; i < elementInfos.length; i++) {
                Object element;
                if (elementInfos[i] instanceof BeanInfo) {
                    element = pico.getComponent(elementInfos[i].getName());
                }
                else if (elementInfos[i] instanceof RefInfo) {
                    element = pico.getComponent(((RefInfo)elementInfos[i]).bean);
                }
                else {
                    element = elementInfos[i].createObject(null);
                }
                list.add(element);
            }
        }
        pico.addComponent(hasName() ? getName() : List.class, list);
    }
}
