package com.bizcreator.util;

public class KeyCodeName extends KeyNamePair {
    private String code;

    public KeyCodeName(Object key, String code, String name) {
        super(key, name);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}