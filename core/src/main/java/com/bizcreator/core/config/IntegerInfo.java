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

import com.bizcreator.core.BaseException;
import com.bizcreator.util.XmlUtil;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;

public class IntegerInfo extends AbstractObjectInfo {

    private static final long serialVersionUID = 1L;
    Integer i = null;

    public IntegerInfo(Element integerElement, ObjectFactoryParser configParser) {
        super(integerElement, configParser);

        String contentText = getValueString(integerElement);
        try {
            i = new Integer(contentText);
        } catch (Exception e) {
            throw new BaseException("content of " + XmlUtil.toString(integerElement) + " could not be parsed as a integer", e);
        }
    }

    public Object createObject(ObjectFactory objectFactory) {
        return i;
    }

    public void addToPico(MutablePicoContainer pico) {
        pico.addComponent(hasName() ? getName() : Integer.class, i);
    }
}
