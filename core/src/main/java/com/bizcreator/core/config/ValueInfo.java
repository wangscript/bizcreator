package com.bizcreator.core.config;

import org.picocontainer.MutablePicoContainer;

public class ValueInfo implements ObjectInfo {

    private static final long serialVersionUID = 1L;
    String name;
    Object value;

    public ValueInfo(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Object createObject(ObjectFactory objectFactory) {
        return value;
    }

    public String getName() {
        return name;
    }

    public boolean hasName() {
        return (name != null);
    }

    public boolean isSingleton() {
        return false;
    }

    public void addToPico(MutablePicoContainer pico) {
        pico.addComponent(name, value);
    }
}
