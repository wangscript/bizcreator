/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.json;

import com.bizcreator.util.ObjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON转换器
 * TODO:
 * 1. 改进JSONObject, 使其能将日期类型(util.Date, sql.Date, sql.Timestamp...)转换为字符串;
 * 2. 针对一个Bean, 当其某字段为也为Bean时, 可以将所选子Bean的字段展平到父Bean的JSONObject中,
 *    也应可以根据所选子Bean字段, 生成一个子JSONObject
 * @author lgh
 */
public class JSONConverter {

    private final static Gson gson = new GsonBuilder().registerTypeAdapter(java.util.Date.class,
            new UtilDateSerializer()).setDateFormat(DateFormat.LONG).create();

    public static String toJsonOfFields(Object obj, String[] fieldNames) {
        Map jsonObj = new HashMap();
        for (String fieldName : fieldNames) {
            jsonObj.put(fieldName, ObjectUtil.getFieldValue(obj, fieldName));
        }
        return gson.toJson(obj);
    }

    public static String toJSON( Object object ) {
        if (object instanceof Jsonizable) {
            return ((Jsonizable)object).toJson();
        }
        else {
            return gson.toJson(object);
        }
    }

    public static String toJson( Object object,  Type typeOfSrc) {
        if (object instanceof Jsonizable) {
            return ((Jsonizable)object).toJson();
        }
        else {
            return gson.toJson(object, typeOfSrc);
        }
    }

    public static void main(String[] args) {
        // json-lib默认不支持java.sql.Date的序列化，要序列化自己的类，实现一个BeanProcessor处理即可
        
        java.sql.Date d = new java.sql.Date(System.currentTimeMillis());

        // 直接序列化
        

        // 序列化含java.sql.Date作为属性值的bean
        HashMap m = new HashMap();
        m.put("date", d);
        
        System.out.println(gson.toJson(m));
    }
}