package com.bizcreator.core.remoting;

public class EJB3Definition extends ServiceDefinition {
    private Class serviceClass;
	
	public EJB3Definition(String serviceName, Class serviceClass) {
		super(serviceName, serviceClass);
		
	}
}
