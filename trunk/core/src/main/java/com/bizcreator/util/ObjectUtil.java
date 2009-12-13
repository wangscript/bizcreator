package com.bizcreator.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bizcreator.core.json.JSONConverter;



public class ObjectUtil {

    static Log log = LogFactory.getLog(ObjectUtil.class);
    
    private final static Map<String, Class> typeMap = new HashMap<String, Class>();
    
    static {
        typeMap.put("String", String.class);
        typeMap.put("Integer", Integer.class);
        typeMap.put("int", int.class);
        typeMap.put("Long", Long.class);
        typeMap.put("long", long.class);
        typeMap.put("Float", Double.class);
        typeMap.put("float", double.class);
        typeMap.put("Double", Double.class);
        typeMap.put("double", double.class);
        
        typeMap.put("Boolean", Boolean.class);
        typeMap.put("boolean", boolean.class);
        
        typeMap.put("Date", java.util.Date.class);
        typeMap.put("Object", Object.class);
        typeMap.put("Array", Object[].class);
    }
    
    public static Class getType(String typeKey) {
        return typeMap.get(typeKey); 
    }
    
    /**
     * 返回指定类的某个属性的getter方法
     * @param clazz
     * @param propertyName 属性名, 可以是多级属性, 如: partyLocation.location
     * @return
     */
    public static Method getGetter(Class clazz, String propertyName) {
        List<String> props = StringUtil.split(propertyName, ".");
        Class cls = clazz;
        Method m = null;
        for (String prop : props) {
            try {
                m = cls.getMethod(getGetterMethodName(prop), new Class[]{});
                cls = m.getReturnType();
            } catch (NoSuchMethodException ex) {
                try {
                    m = cls.getMethod(getBooleanGetterMethodName(prop), new Class[]{});
                    cls = m.getReturnType();
                }
                catch (NoSuchMethodException ne) {
                    ne.printStackTrace();
                }
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
        }
        return m;
    }
    
    /**
     * 返回字段的类型, 支持多级字段
     * @param clazz
     * @param propertyName
     * @return
     */
    public static Class getFieldType(Class clazz, String propertyName) {
        List<String> props = StringUtil.split(propertyName, ".");
        Class cls = clazz;
        try {
            for (String prop : props) {
                Method m = cls.getMethod(getGetterMethodName(prop), new Class[]{});
                cls = m.getReturnType();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            cls = null;
        } 
        return cls;
    }
    
    public static Collection getGetters(Class classToAnalyze) {
        ArrayList result = new ArrayList();

        Method[] methods = classToAnalyze.getMethods(); //classToAnalyze.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (isGetter(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public static boolean isGetter(Method method) {
        String methodName = method.getName();

        //排除get(...)方法
        if (methodName.equals("get") || methodName.equals("is")) {
            return false;
        }
        
        if (!methodName.startsWith("get") &&
            !methodName.startsWith("is")) {
            return false;
        }

        if (methodName.startsWith("is") && (!method.getReturnType().equals(boolean.class))) {
            return false;
        }

        if (method.getReturnType().equals(void.class)) {
            return false;
        }

        if (method.getParameterTypes().length != 0) {
            return false;
        }

        return true;

    }

    public static Collection getSetters(Class classToAnalyze) {
        ArrayList result = new ArrayList();

        Method[] methods = classToAnalyze.getMethods(); //classToAnalyze.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if (isSetter(method)) {
                result.add(method);
            }
        }
        return result;
    }

    public static boolean isSetter(Method method) {
        String methodName = method.getName();

        if (methodName.equals("set")) { //排除set(...)方法
            return false;
        }
        
        if (!methodName.startsWith("set")) {
            return false;
        }

        if (method.getParameterTypes().length != 1) {
            return false;
        }

        return true;
    }


    public static String getPropertyName(String getterOrSetterName) {
        if (getterOrSetterName.startsWith("set") ||
            getterOrSetterName.startsWith("get")) {
            return decapitalize(getterOrSetterName.substring(3));
        }
        if (getterOrSetterName.startsWith("is")) {
            return decapitalize(getterOrSetterName.substring(2));
        }
        throw new IllegalArgumentException(getterOrSetterName +
                                           " is not a correct getter or setter"
                                           + "name");
    }

    public static String getFieldName(String getterOrSetterName) {
        return getPropertyName(getterOrSetterName);
    }

    /**
     * Utility method to take a string and convert it to normal Java variable
     * name capitalization.  This normally means converting the first
     * character from upper case to lower case, but in the (unusual) special
     * case when there is more than one character and both the first and
     * second characters are upper case, we leave it alone.
     * <p>
     * Thus "FooBah" becomes "fooBah" and "X" becomes "x", but "URL" stays
     * as "URL".
     *
     * @param  name The string to be decapitalized.
     * @return  The decapitalized version of the string.
     */
    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
            Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    public static String getSetterMethodName(String propertyName) {
        return "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }


    public static String getBooleanGetterMethodName(String propertyName) {
        return "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }


    public static String getGetterMethodName(String propertyName) {
        return "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
    }


    public static void copyFields(Object from, Object to) {
        Collection getters = ObjectUtil.getGetters(from.getClass());

        for (Iterator iterator = getters.iterator(); iterator.hasNext(); ) {
            Method getter = (Method) iterator.next();

            String propertyName = ObjectUtil.getPropertyName(getter.getName());

            try {
                Method setter = to.getClass().getMethod(ObjectUtil.
                                                                getSetterMethodName(propertyName),
                                                                new Class[] {getter.getReturnType()});

                Object getterResult = getter.invoke(from, new Object[0]);

                setter.invoke(to, new Object[] {getterResult});
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            catch (IllegalAccessException ex) {
                continue;
            }
            catch (InvocationTargetException ex) {
                continue;
            }
        }
    }

    /** --------- 将所有的字段设为null ------------*/
    public static void emptyFields(Object obj) {
        Collection setters = ObjectUtil.getSetters(obj.getClass());
        for (Iterator iterator = setters.iterator(); iterator.hasNext(); ) {
            Method setter = (Method) iterator.next();
            try {
                Class[] paramCls = setter.getParameterTypes();
                if (paramCls[0] == boolean.class) {
                    //
                    setter.invoke(obj, new Object[] {Boolean.FALSE});
                }
                else {
                    setter.invoke(obj, new Object[] {null});
                }
            }
            catch (IllegalAccessException ex) {
                continue;
            }
            catch (InvocationTargetException ex) {
                continue;
            }
        }
    }
    
    /**
     * 获取对象的字段的值，支持多级字段链，如：getFeildValue(party, "parentParty.name")
     * 返回party对象的parentParty字段的name的值
     * @param obj
     * @param fieldChain
     * @return
     */
    public static Object getFieldValue(Object obj, String fieldChain) {

        List<String> fieldNames = StringUtil.split(fieldChain, ".");

        Object retObj = obj;
        for (String fieldName : fieldNames) {
            if (retObj == null) {
                return null;
            }
            retObj = getValue(retObj, fieldName);
        }

        return retObj;
    }
    
    /**
     * 获取对象字段的值，不支持多级字段链
     * @param obj
     * @param fieldName
     * @return
     */
    private static Object getValue(Object obj, String fieldName) {
        Class clazz = obj.getClass();
        Method getter = getGetter(clazz, fieldName);
        if (getter != null) {
            try {
                return getter.invoke(obj, new Object[0]);
            }
            catch (InvocationTargetException ex) {
                ex.printStackTrace();
                return null;
            }
            catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                return null;
            }
            catch (IllegalAccessException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        /*
        Method[] methods = clazz.getMethods(); //clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method getter = methods[i];
            if (getter.getName().equals(getGetterMethodName(fieldName)) ||
                    getter.getName().equals(getBooleanGetterMethodName(fieldName))) {
                //try ...

            }
        }*/
        return null;
    }
    
    /**
     * 设置某个对象字段的值，支持多级字段链
     * 如：setFieldValue(party, "parentParty.name", "microsoft")
     * @param obj
     * @param fieldChain
     * @param fieldValue
     */
    public static Object setFieldValue(Object obj, String fieldChain, Object fieldValue) {

        List<String> fieldNames = StringUtil.split(fieldChain, ".");

        Object retObj = obj;
        Object lastObj = obj;
        int size = fieldNames.size();

        Class clazz = null;
        String fieldName = null;
        Field field = null;
        Class fieldClass = null;

        for (int i = 0; i < size - 1; i++) {
            fieldName = fieldNames.get(i);
            //log.fine("handle field value: " + fieldName + " of " + lastObj);
            retObj = getValue(lastObj, fieldName);

            //如果retObj为空，则构造一个新的实例
            if (retObj == null) {
                clazz = lastObj.getClass();
                try {
                    field = getField(clazz, fieldName);
                    field.setAccessible(true);
                    fieldClass = field.getType();
                    retObj = fieldClass.newInstance();
                    setValue(lastObj, fieldName, retObj);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    return null;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            lastObj = retObj;
        }
        if (retObj != null) {
            return setValue(retObj, fieldNames.get(size - 1), fieldValue);
        } else {
            return null;
        }
    }
    
    /**
     * 设置某个对象字段的值，不支持多级字段链
     * 如：setFieldValue(party, "name", "microsoft")
     * @param obj
     * @param fieldName
     * @param fieldValue
     */
    private static Object setValue(Object obj, String fieldName, Object fieldValue) {

        Object oldValue = null;
        Class clazz = obj.getClass();
        Method getter = getGetter(clazz, fieldName);
        if (getter != null) {
            try {
                
                if (fieldValue instanceof BigDecimal) {
                    if (getter.getReturnType() == double.class) {
                        fieldValue = ((BigDecimal) fieldValue).doubleValue();
                    } else if (getter.getReturnType() == float.class) {
                        fieldValue = ((BigDecimal) fieldValue).floatValue();
                    } else if (getter.getReturnType() == long.class) {
                        fieldValue = ((BigDecimal) fieldValue).longValue();
                    } else if (getter.getReturnType() == int.class) {
                        fieldValue = ((BigDecimal) fieldValue).intValue();
                    } else if (getter.getReturnType() == byte.class) {
                        fieldValue = ((BigDecimal) fieldValue).byteValue();
                    }
                }
                else if (fieldValue instanceof Double) {
                    if (getter.getReturnType() == double.class) {
                        fieldValue = ((Double) fieldValue).doubleValue();
                    } else if (getter.getReturnType() == float.class) {
                        fieldValue = ((Double) fieldValue).floatValue();
                    } else if (getter.getReturnType() == long.class) {
                        fieldValue = ((Double) fieldValue).longValue();
                    } else if (getter.getReturnType() == int.class) {
                        fieldValue = ((Double) fieldValue).intValue();
                    } else if (getter.getReturnType() == byte.class) {
                        fieldValue = ((Double) fieldValue).byteValue();
                    }
                }
                oldValue = getter.invoke(obj, new Object[0]);
                Method setter = clazz.getMethod(getSetterMethodName(fieldName),
                        new Class[]{getter.getReturnType()});
                setter.invoke(obj, new Object[]{fieldValue});
            } catch (SecurityException ex) {
                ex.printStackTrace();
                //log.debug(ex);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
                //log.debug(ex);
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
                //log.debug(ex);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                //log.debug(ex);
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                //log.debug(ex);
            }
        }
        /*
        Method[] methods = clazz.getMethods(); //clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method getter = methods[i];
            if (getter.getName().equals(getGetterMethodName(fieldName)) ||
                    getter.getName().equals(getBooleanGetterMethodName(fieldName))) {
                //try {}catch(...)
            }
        }*/
        return oldValue;
    }
    
    public static Field getField(Class clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Class superCls = clazz.getSuperclass();
            if (superCls != null) {
                field = getField(superCls, fieldName);
            }
        }
        return field;
    }
    
    
    /**
     * 从一个对象取出各属性的值, 赋给另一个对象相同的属性. 
     * 通常用于将父类的各字段值赋给子类, 如: TransportOrder -> TransportOrderQe,
     * 以便将服务器端取得的hibernate对象存于客户端的表格对象中, 作为缓存对象.
     * @param fromObj
     * @param toObj
     */
    public static void assignObject(Object fromObj, Object toObj) throws IllegalArgumentException, IllegalAccessException {
        if (fromObj == null || toObj == null) return;
        copyFields(fromObj, toObj);
    }
    
    /**
     * 从一个对象提取另一个类型的值, 如: 从TransportOrderQe中提取父类TransportOrder,
     * 以便送到服务器进行hibernate的持久化.
     * @param fromObj
     * @param toClass
     * @return
     */
    public static Object extractObject(Object fromObj, Class toClass) throws InstantiationException, IllegalAccessException {
        if (fromObj == null || toClass == null) {
            return null;
        }
        Collection getters = ObjectUtil.getGetters(toClass);
        Object toObj = toClass.newInstance();
        for (Iterator iterator = getters.iterator(); iterator.hasNext(); ) {
            Method getter = (Method) iterator.next();
            String propertyName = ObjectUtil.getPropertyName(getter.getName());
            getter = getGetter(fromObj.getClass(), propertyName);
            try {
                //System.out.println(">>>prop name: " + propertyName + ", getter: " + getter);
                Method setter = toClass.getMethod(ObjectUtil.getSetterMethodName(propertyName),
                        new Class[] {getter.getReturnType()});

                Object getterResult = getter.invoke(fromObj, new Object[0]);

                setter.invoke(toObj, new Object[] {getterResult});
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            catch (IllegalAccessException ex) {
                continue;
            }
            catch (InvocationTargetException ex) {
                continue;
            }
        }
        return toObj;
    }
    
    /**
     * 将对象的某几个字段转换成Map数据
     * @param obj
     * @param fldNams
     * @return
     */
    public static Map getMapData(Object obj, String[] fldNames) {
        Map map = new HashMap();
        for (String fldName : fldNames) {
            map.put(fldName, getFieldValue(obj, fldName));
        }
        return map;
    }
    
    /**
     * 利用Map数据更新obj字段值
     * @param obj
     * @param map
     */
    public static void updateObject(Object obj, Map map) {
        Set<Map.Entry> entries = map.entrySet();
        for (Map.Entry entry : entries) {
            Class objCls = obj.getClass();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            Class valCls = value.getClass();
            //如果键值为Map，则可能需要递归设值
            if (value != null && Map.class.isAssignableFrom(valCls)) {
                Class keyCls = getFieldType(objCls, key);
                if (keyCls != null && Map.class.isAssignableFrom(keyCls)) {
                    //如字段本身为Map类型，则直接设值
                    setFieldValue(obj, key, value);
                }
                else if (keyCls != null) {
                    Object keyObj = getFieldValue(obj, key);
                    if (keyObj == null) {
                        try {
                            keyObj = keyCls.newInstance();
                            setFieldValue(obj, key, keyObj);
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (keyObj != null) {
                        updateObject(keyObj, (Map) value);
                    }
                }
            }
            else {
                setFieldValue(obj, key, value);
            }
        }
    }

    /**
     * 将map型参数转换为数组型参数
     * @param paramMap
     * @return
     */
    public static Object[] fromParamMap(Map paramMap) {
        if (paramMap == null || paramMap.size() == 0) return null;
        Object[] paramPairs = new Object[paramMap.size()*2];
        Set<Map.Entry> entries = paramMap.entrySet();
        int i=0;
        for (Map.Entry entry : entries) {
            paramPairs[i++] = entry.getKey();
            paramPairs[i++] = entry.getValue();
        }
        return paramPairs;
    }
    
    //-------------------
    /**
     * 判断对象是否为集合类型
     * @param obj
     * @return
     */
    public static boolean isCollection(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (obj.getClass().isArray() || Collection.class.isAssignableFrom(obj.getClass())) {
            return true;
        }
        else return false;  
    }
    
    public static Method findMethod(Class clazz, String methodName, String[] types) {
        Class[] paramTypes = getActParamTypes(types);
        return findMethod(clazz, methodName, paramTypes);
    }

    public static Method findMethod(Class clazz, String methodName, Class[] paramTypes) {
        
        try {
            Method mt = clazz.getMethod(methodName, paramTypes);
            if (mt != null) return mt;
        }
        catch (Exception ex) {
            //ex.printStackTrace();
        }
        
        try {
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                boolean matched = false;
                Class[] types = m.getParameterTypes();
                if (Modifier.isPublic(m.getModifiers()) && types.length == paramTypes.length && m.getName().equals(methodName)) {
                    matched = true;
                    for (int i = 0; i < types.length; i++) {
                        if (isTypeIdentical(types[i], paramTypes[i])) {
                            continue;
                        }
                        if (!types[i].isAssignableFrom(paramTypes[i])) {
                            matched = false;
                            break;
                        }
                    }
                    if (matched) {
                        return m;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }
  
    public static boolean isTypeIdentical(Class type1, Class type2) {
        if ((type1 == byte.class || type1 == Byte.class) && (type2 == byte.class || type2 == Byte.class)) {
            return true;
        } else if ((type1 == short.class || type1 == Short.class) && (type2 == short.class || type2 == Short.class)) {
            return true;
        } else if ((type1 == int.class || type1 == Integer.class) && (type2 == int.class || type2 == Integer.class)) {
            return true;
        } else if ((type1 == long.class || type1 == Long.class) && (type2 == long.class || type2 == Long.class)) {
            return true;
        } else if ((type1 == float.class || type1 == Float.class) && (type2 == float.class || type2 == Float.class)) {
            return true;
        }
        else if ((type1 == double.class || type1 == Double.class) && (type2 == double.class || type2 == Double.class)) {
            return true;
        }
        else return false;
    }
    
    public static Class[] getActParamTypes(String[] types) {
        Class[] paramTypes = new Class[types.length];
        Class cls = null;
        for (int i = 0; i < types.length; i++) {
            cls = getType(types[i]);
            if (cls == null) {
                try {
                    cls = Class.forName(types[i]);
                } catch (Exception ex) {
                    cls = Object.class;
                }
            }
            paramTypes[i] = cls;
        }
        return paramTypes;
    }
 
    public static Object getActParamValue(String s, Class paramType) {
        if (paramType == String.class) {
            return s;
        } else if (paramType == Integer.class || paramType == int.class) {
            return Integer.parseInt(s);
        } else if (paramType == Float.class || paramType == float.class) {
            return Float.parseFloat(s);
        } else if (paramType == Float.class || paramType == float.class) {
            return Double.parseDouble(s);
        }else if (paramType == Boolean.class || paramType == boolean.class) {
            return Boolean.parseBoolean(s);
        } else if (paramType == java.util.Date.class) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(s);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                //JSONObject jsonObj = JSONObject.fromObject(s);
                Object obj = JSONConverter.gson().fromJson(s, paramType);
                return obj;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
