/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.json;

import net.sf.json.JSONObject;

/**
 *
 * @author lgh
 */
public interface Jsonizable {


    public JSONObject toJSON();

    public Object fromJSON(JSONObject json);
    
}
