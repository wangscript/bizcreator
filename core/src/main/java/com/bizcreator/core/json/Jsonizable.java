/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.json;

import com.google.gson.JsonObject;

/**
 *
 * @author lgh
 */
public interface Jsonizable {

    public JsonObject toJson();

    public Object fromJson(BizJsonObject json);
    
}
