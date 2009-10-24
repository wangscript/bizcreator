/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.pool;

/**
 *
 * @author lgh
 */
public class StatelessSessionInstancePool extends AbstractInstancePool {
    
    public StatelessSessionInstancePool(String serviceName, Class serviceClass) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }
    
}
