/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

/**
 *
 * @author Administrator
 */
public abstract class ServiceLocator {
    
    public abstract Object lookup(String serviceName);
    
}
