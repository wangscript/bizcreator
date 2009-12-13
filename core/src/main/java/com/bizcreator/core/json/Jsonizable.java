/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.json;

/**
 *
 * @author lgh
 */
public interface Jsonizable {

    public String toJson();

    public Object fromJson(String json);
    
}
