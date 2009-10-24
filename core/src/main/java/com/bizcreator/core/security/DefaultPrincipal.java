package com.bizcreator.core.security;

import java.io.Serializable;
import java.security.Principal;

public class DefaultPrincipal implements Principal, Serializable {

    private String name;

    public DefaultPrincipal(String name) {
        if (name == null) throw new NullPointerException();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DefaultPrincipal)) return false;
        return ((DefaultPrincipal) obj).getName().equals(name);
    }

    public String toString() {
        return name;
    }
}