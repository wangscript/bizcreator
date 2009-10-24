/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;

/**
 *
 * @author lgh
 */
public class StatefulSessionInstancePool extends AbstractInstancePool {

    public StatefulSessionInstancePool(String serviceName, Class serviceClass) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }
    
    @Override
    public synchronized void free(ServiceContext ctx) {
        discard(ctx);
    }
}
