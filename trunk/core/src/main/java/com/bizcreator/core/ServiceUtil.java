/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.session.ServiceBase;
import com.bizcreator.core.session.StatefulService;
import javax.ejb.Stateful;

/**
 *
 * @author lgh
 */
public class ServiceUtil {
    
    public static boolean isStateful(Class serviceInterface) {
        boolean result = false;
        result = serviceInterface.isAnnotationPresent(Stateful.class) || StatefulService.class.isAssignableFrom(serviceInterface);
        return result;
    }
    
    public static Class getServiceInterface(Class clazz) {
        Class[] classes = clazz.getInterfaces();
        for (Class cls : classes) {
            if (ServiceBase.class.isAssignableFrom(cls)) {
                return cls;
            }
        }
        return null;
    }
}
