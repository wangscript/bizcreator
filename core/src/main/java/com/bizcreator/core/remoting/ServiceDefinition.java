package com.bizcreator.core.remoting;

public class ServiceDefinition implements java.io.Serializable {
	
	private String serviceName;
    private Class serviceInterface;
    
	//服务id: 对于Stateful service, 该id表示对应的session id
    private Object id;
    
	public ServiceDefinition(String serviceName, Class serviceInterface) {
		this.serviceName = serviceName;
        this.serviceInterface = serviceInterface;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
    public Class getServiceInterface() {
		return serviceInterface;
	}

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
    
    @Override
	public int hashCode() {
        return serviceName.hashCode();
    }
	
    @Override
	public String toString() {
		return serviceName;
	}
	
    @Override
    public boolean equals(Object obj) {
		if (obj instanceof ServiceDefinition) {
			ServiceDefinition that = (ServiceDefinition)obj;
			if (!this.getServiceName().equals(that.getServiceName())) {
				return false;
			}
			return true;
		}
		return false;
	}
	
}
