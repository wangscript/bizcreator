/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.json;

import com.bizcreator.util.ObjectUtil;
import java.util.HashMap;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

/**
 * JSON转换器
 * TODO:
 * 1. 改进JSONObject, 使其能将日期类型(util.Date, sql.Date, sql.Timestamp...)转换为字符串;
 * 2. 针对一个Bean, 当其某字段为也为Bean时, 可以将所选子Bean的字段展平到父Bean的JSONObject中,
 *    也应可以根据所选子Bean字段, 生成一个子JSONObject
 * @author lgh
 */
public class JSONConverter {

    public static JSONObject toJSON(Object obj, String[] fieldNames) {
        JSONObject jsonObj = new JSONObject();
        for (String fieldName : fieldNames) {
            jsonObj.put(fieldName, ObjectUtil.getFieldValue(obj, fieldName));
        }
        return jsonObj;
    }

    public static JSONObject toJSON( Object object ) {
        return toJSON(object, new JsonConfig());
    }

    public static JSONObject toJSON( Object object, JsonConfig jsonConfig ) {
        if (object instanceof Jsonizable) {
            return ((Jsonizable)object).toJSON();
        }
        else {
            return JSONObject.fromObject(object, jsonConfig);
        }
    }

    public static void main(String[] args) {
        // json-lib默认不支持java.sql.Date的序列化，要序列化自己的类，实现一个BeanProcessor处理即可
        JsDateJsonBeanProcessor beanProcessor = new JsDateJsonBeanProcessor();
        java.sql.Date d = new java.sql.Date(System.currentTimeMillis());

        // 直接序列化
        JsonConfig config = new JsonConfig();
        JSONObject json = beanProcessor.processBean(d, config);
        System.out.println(json.toString());

        // 序列化含java.sql.Date作为属性值的bean
        HashMap m = new HashMap();
        m.put("date", d);
        config.registerJsonBeanProcessor(java.sql.Date.class, beanProcessor);
        json = JSONObject.fromObject(m, config);
        System.out.println(json.toString());
    }
}