/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import com.bizcreator.core.entity.AtomicEntity;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.TopQe;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 查询过滤管理
 * @author Administrator
 */
public class QueryFactory  {//////

    static Log log = LogFactory.getLog(QueryFactory.class);

    public final static String QL_SELECT = "select";
    public final static String QL_FROM = "from";
    public final static String QL_JOIN = "join";
    public final static String QL_WHERE = "where";
    public final static String QL_ORDER_BY = "orderBy";

    public final static String TYPE_ATOMIC = "atomic";
    public final static String TYPE_BASIC = "basic";
    public final static String TYPE_NORMAL = "normal";
    public final static String TYPE_DELETE = "delete";

    private static Map<Class, Map<String, StringBuffer>> qlParts = new HashMap<Class, Map<String, StringBuffer>>();

    private static Map<Class, Map<String, String>> filterMap = new HashMap<Class, Map<String, String>>();

    //将后缀为_like的过滤字段另外保存, 用于RhLookupField的搜索
    private static Map<Class, List<String>> likeFilterMap = new HashMap<Class, List<String>>();

    public QueryFactory(){}


    //查询目标实体
    private static Map<Class, Class> defaultFromClasses = new HashMap<Class, Class>();
    public static void setDefaultFromClass(Class qeClass, Class fromClass) {
        defaultFromClasses.put(qeClass, fromClass);
    }

    public static Class getDefaultFromClass(Class qeClass) {
        return defaultFromClasses.get(qeClass);
    }

    //------------------------- 查询语句 --------------------------
    public static String getQL(Class qeClass, Class fromClass) {
        return getQL(qeClass, fromClass, (Object[]) null);
    }

    public static String getQL(Class qeClass, Class fromClass, String qlType) {
        return getQL(qeClass, fromClass, (Object[]) null, qlType);
    }

    public static String getQL(Class qeClass, Object[] paramPairs) {
        return getQL(qeClass, (Class) null, paramPairs);
    }

    public static String getQL(Class qeClass, Object[] paramPairs, String qlType) {
        return getQL(qeClass, (Class) null, paramPairs, qlType);
    }

    public static String getQL(Class qeClass) {
        return getQL(qeClass, (Class) null, (Object[])null);
    }

    public static String getQL(Class qeClass, String qlType) {
        return getQL(qeClass, (Class) null, (Object[]) null, qlType);
    }

    //-----------------------------------------------------------------------
    public static String getQL(Class qeClass, Class fromClass, Object[] paramPairs) {
        return getQL(qeClass, fromClass, null, paramPairs);
    }

    public static String getQL(Class qeClass, Class fromClass, Object[] paramPairs, String qlType) {
        return getQL(qeClass, fromClass, null, null, paramPairs, qlType);
    }

    //------------------------------------------------------------------------
    //缓存已构建的ql语句串
    private static Map<Class, StringBuffer> qlMap = new HashMap<Class, StringBuffer>();

    /**
     * 返回qeClass的实例对象列表
     * @param qeClass 返回的查询实体类
     * @param fromClass
     * @param extraFrom
     * @param filter
     * @param paramPairs
     * @param qlType
     * @return
     */
    public static String getQL(Class qeClass, Class fromClass, String extraFrom, String filter, Object[] paramPairs, String qlType) {
        
        //----------------------------------------------------
        //1. 构造select语句
        StringBuffer ql = new StringBuffer("");
        if (TYPE_ATOMIC.equals(qlType)) {
            ql.append("select new " + AtomicEntity.class.getName()
                    + "(" + getQlPart(AtomicEntity.class, QL_SELECT) + ")");
        }
        else if (TYPE_BASIC.equals("qlType")) {
            ql.append("select new " + BasicEntity.class.getName()
                    + "(" + getQlPart(BasicEntity.class, QL_SELECT) + ")");
        }
        else if (getQlSelectEx(qeClass) != null) {
            /*
            if (qeClass != fromClass) {
                ql.append("select new " + qeClass.getName() + "(" + getQlSelectEx(qeClass) + ")");
            }
            else {
                ql.append("select " + getQlSelectEx(qeClass));
            }*/
            ql.append("select new " + qeClass.getName() + "(" + getQlSelectEx(qeClass) + ")");
        }
        else {
            //不能直接使用 from 语句, 如果缺少select o, 则会由于join而从join的多个实体选择,
            //数组的形式返回
            ql.append("select o ");
        }
        return getQL(ql.toString(), qeClass, fromClass, extraFrom, filter, paramPairs);
    }

    public static String getQL(Class qeClass, Class fromClass, String filter, Object[] paramPairs, String qlType) {
        return getQL(qeClass, fromClass, null, filter, paramPairs, qlType);
    }
    public static String getQL(Class qeClass, Class fromClass, String filter, Object[] paramPairs) {
        return getQL(qeClass, fromClass, null, filter, paramPairs, TYPE_NORMAL);
    }
    public static String getQL(Class qeClass, Class fromClass, String extraFrom, String filter, Object[] paramPairs) {
        return getQL(qeClass, fromClass, extraFrom, filter, paramPairs, TYPE_NORMAL);
    }



    //-------------------------------------------------------------------------
   /**
    *
    * 选择fields数组中的字段值返回，不需要相应的qeClass的构造函数来实例化对象,
    * 执行该方法需要在相关的qeClass中设置对象字段到数据库列(实体选择列)的映射.
    * @param fields 需查询的字段数组
    * @param fromClass
    * @param extraFrom
    * @param filter
    * @param paramPairs
    * @param qlType
    * @return
    */
    public static String getQL(String[] fields, Class qeClass, Class fromClass, String extraFrom, String filter,
            Object[] paramPairs, String qlType) {

        StringBuffer sb = new StringBuffer("select ");

        //1. 从field2columnMap中获取对应的查询列
        for (String fieldName : fields) {
            String column = getFieldColumnEx(qeClass, fieldName);
            if (column == null) {
                if (fieldName.endsWith("Id")) {
                    int pos = fieldName.indexOf("Id");
                    column = fieldName.substring(0, pos) + ".id";
                }
                else if (fieldName.equals("Name")) {
                    int pos = fieldName.indexOf("Name");
                    column = fieldName.substring(0, pos) + ".name";
                }
                else {
                    column = "o." + fieldName;
                }
            }
            sb.append(column + ", ");
        }
        String qlSelect = sb.toString();
        if (qlSelect.endsWith(", ")) {
            qlSelect = qlSelect.substring(0, qlSelect.length()-2);
        }
        else {
            qlSelect = "";
        }

        //2. 通过已有的方法构造QL
        return getQL(qlSelect, qeClass, fromClass, extraFrom, filter, paramPairs);
    }

    public static String getQL(String[] fields, Class qeClass, Class fromClass, String extraFrom,
            String filter, Object[] paramPairs) {
        return getQL(fields, qeClass, fromClass, extraFrom, filter, paramPairs, TYPE_NORMAL);
    }

    public static String getQL(String[] fields, Class qeClass, Class fromClass, String filter,
            Object[] paramPairs) {
        return getQL(fields, qeClass, fromClass, null, filter, paramPairs, TYPE_NORMAL);
    }

    public static String getQL(String[] fields, Class fromClass, String filter, Object[] paramPairs) {
        if (fromClass == null) return null;
        return getQL(fields, fromClass, fromClass, null, filter, paramPairs, TYPE_NORMAL);
    }

    //-------------------------------------------------------------------------------------
    /**
     * 通过传入的"select ..."串和其他参数构建ql语句
     * @param qlSelect 型如: select new entity(...) 的select字符串
     * @param qeClass 查询实体类(query entity class)
     * @param fromClass 被查询的持久化实体类
     * @param extraFrom 被查询的其他持久化实体类
     * @param filter 传入的过滤条件
     * @param paramPairs 查询参数对<参数名称, 参数值>.
     * @return
     */
    public static String getQL(String qlSelect, Class qeClass, Class fromClass, String extraFrom,
            String filter, Object[] paramPairs) {

        //1. 已确定的qlSelect
        StringBuffer ql = new StringBuffer(qlSelect);

        //---------------------------------------------------------
        //2. 构造 from 语句
        if (fromClass != null) {
            ql.append(" from " + fromClass.getName() + " o");
        }
        else if (getDefaultFromClass(qeClass) != null) {
            ql.append(" from " + getDefaultFromClass(qeClass).getName() + " o");
        }
        else {
            ql.append(" from " + qeClass.getName() + " o");
        }

        if (getQlFromEx(qeClass) != null) {
            ql.append(", " + getQlFromEx(qeClass) + " ");
        }

        if (extraFrom != null) {
            ql.append(", " + extraFrom);
        }

        if (getQlJoinEx(qeClass) != null) {
            ql.append(getQlJoinEx(qeClass));
        }

        //---------------------------------------------------
        //3. 构造 where 语句
        if (getQlWhereEx(qeClass) != null) {
            ql.append(" where " +  getQlWhereEx(qeClass));
        }
        else {
            ql.append(" where 1=1");
        }

        //----------------------------------------------------
        //4. 增加过滤条件
        if (filter != null) {
            ql.append(" and " + filter);
        }

        //-----------------------------------------------------
        //5. 增加过滤参数
        //追加paramPairs中的参数字段作为查询条件
        if (paramPairs != null) {
            List<String> flds = new ArrayList<String>();
            for (int i=0; i<paramPairs.length; i=i+2) {
                //过滤字段
                String paramName = paramPairs[i].toString();
                String fld = getFilterFieldEx(qeClass, paramName);
                if(fld != null) {
                    if (paramName.endsWith("_gt")) {
                        ql.append(" and " + fld + " > :" + paramName);
                    }
                    else if (paramName.endsWith("_ge")){
                        ql.append(" and " + fld + " >= :" + paramName);
                    }
                    else if (paramName.endsWith("_lt")){
                        ql.append(" and " + fld + " < :" + paramName);
                    }
                    else if (paramName.endsWith("_le")){
                        ql.append(" and " + fld + " <= :" + paramName);
                    }
                    else if (paramName.endsWith("_like")){
                        ql.append(" and " + fld + " like :" + paramName);
                    }
                    else if (paramName.endsWith("_in")) {
                        ql.append(" and " + fld + " in ( :" + paramName + ")");
                    }
                    else {
                        ql.append(" and " + fld + "=:" + paramName);
                    }
                    flds.add(paramPairs[i].toString());
                }
            }
        }

        if (getQlOrderByEx(qeClass) != null) {
            ql.append(" order by " +  getQlOrderByEx(qeClass));
        }
        System.out.println("--------------------------------------------------");
        System.out.println(">>>ql: " + ql.toString());
        System.out.println("--------------------------------------------------");

        //6. 返回结果
        return ql.toString();
    }

    public static String getQL(String qlSelect, Class fromClass, String filter, Object[] paramPairs) {
        if (fromClass == null) return null;
        return getQL(qlSelect, fromClass, fromClass, null, filter, paramPairs);
    }


    public static StringBuffer getQlPart(Class qeClass, String partKey) {

        //对BasicEntity必须额外进行初始化
        Map<String, StringBuffer> qeMap = qlParts.get(BasicEntity.class);
        if (qeMap == null) {
            BasicEntity.init();
        }

        qeMap = qlParts.get(qeClass);
        if (qeMap == null) {
            init(qeClass);
            qeMap = qlParts.get(qeClass);
        }

        if (qeMap == null) return null;
        else {
            return qeMap.get(partKey);
        }
    }

    public static StringBuffer getQlPartEx(Class qeClass, String partKey) {
        return null;
    }

    private static Map<Class, StringBuffer> qlSelects = new HashMap<Class, StringBuffer>();
    public static StringBuffer getQlSelectEx(Class qeClass) {
        StringBuffer qlSelect = qlSelects.get(qeClass);
        if (qlSelect != null) return qlSelect;
        else {
            qlSelect = new StringBuffer("");
        }

        List<Class> classes = new ArrayList<Class>();
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            classes.add(c);
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        Collections.reverse(classes);

        for (Class clazz : classes) {
            StringBuffer sb = qlSelects.get(clazz);
            if (sb == null) {
                sb = getQlPart(clazz, QL_SELECT);
                StringBuffer ssb = new StringBuffer(qlSelect);
                if (sb != null) {
                    if (ssb.length() > 0)  ssb.append(", " + sb); else ssb.append(sb);
                    if (qlSelect.length() > 0) qlSelect.append(", " + sb); else qlSelect.append(sb);
                    qlSelects.put(clazz, ssb);
                }
            }
            else {
                qlSelect = new StringBuffer(sb);
            }
        }
        if (qlSelect.length() == 0) {
            qlSelect = null;
        }
        return qlSelect;
    }

    /**
     * 是否直接实现了TopQe接口
     * @param clazz
     * @return
     */
    private static boolean isDirectImplTopQe(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces != null) {
            for (Class intf : interfaces) {
                if (intf == TopQe.class) return true;
            }
        }
        return false;
    }

    
    private static Map<Class, StringBuffer> qlFroms = new HashMap<Class, StringBuffer>();
    public static StringBuffer getQlFromEx(Class qeClass) {

        StringBuffer qlFrom = qlFroms.get(qeClass);
        if (qlFrom != null) return qlFrom;
        else {
            qlFrom = new StringBuffer("");
        }

        List<Class> classes = new ArrayList<Class>();
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            classes.add(c);
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        Collections.reverse(classes);

        for (Class clazz : classes) {
            StringBuffer sb = qlFroms.get(clazz);
            if (sb == null) {
                sb = getQlPart(clazz, QL_FROM);
                StringBuffer ssb = new StringBuffer(qlFrom);
                if (sb != null) {
                    if (ssb.length()>0) ssb.append(", " + sb); else ssb.append(sb);
                    if (qlFrom.length()>0) qlFrom.append(", " + sb); else qlFrom.append(sb);
                    qlFroms.put(clazz, ssb);
                }
            }
            else {
                qlFrom = new StringBuffer(sb);
            }
        }
        if (qlFrom.length() == 0) {
            qlFrom = null;
        }
        return qlFrom;
    }

    private static Map<Class, StringBuffer> qlJoins = new HashMap<Class, StringBuffer>();
    public static StringBuffer getQlJoinEx(Class qeClass) {

        StringBuffer qlJoin = qlJoins.get(qeClass);
        if (qlJoin != null) return qlJoin;
        else {
            qlJoin = new StringBuffer("");
        }

        List<Class> classes = new ArrayList<Class>();
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            classes.add(c);
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        Collections.reverse(classes);

        for (Class clazz : classes) {
            StringBuffer sb = qlJoins.get(clazz);
            if (sb == null) {
                sb = getQlPart(clazz, QL_JOIN);
                StringBuffer ssb = new StringBuffer(qlJoin);
                if (sb != null) {
                    ssb.append(" " + sb);
                    qlJoin.append(" " + sb);
                    qlJoins.put(clazz, ssb);
                }
            }
            else {
                qlJoin = new StringBuffer(sb);
            }
        }

        if (qlJoin.length() == 0) {
            qlJoin = null;
        }
        return qlJoin;
    }

    private static Map<Class, StringBuffer> qlWheres = new HashMap<Class, StringBuffer>();
    public static StringBuffer getQlWhereEx(Class qeClass) {

        StringBuffer qlWhere = qlWheres.get(qeClass);
        if (qlWhere != null) return qlWhere;
        else {
            qlWhere = new StringBuffer("");
        }

        List<Class> classes = new ArrayList<Class>();
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            classes.add(c);
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        Collections.reverse(classes);

        for (Class clazz : classes) {
            StringBuffer sb = qlWheres.get(clazz);
            if (sb == null) {
                sb = getQlPart(clazz, QL_WHERE);
                StringBuffer ssb = new StringBuffer(qlWhere);
                if (sb != null) {
                    if (ssb.length()>0) ssb.append(" and " + sb); else ssb.append(sb);
                    if (qlWhere.length()>0) qlWhere.append(" and " + sb); else qlWhere.append(sb);
                    qlWheres.put(clazz, ssb);
                }
            }
            else {
                qlWhere = new StringBuffer(sb);
            }
        }
        if (qlWhere.length() == 0) {
            qlWhere = null;
        }
        return qlWhere;
    }

    public static StringBuffer getQlOrderByEx(Class qeClass) {
        //StringBuffer qlOrderBy = getQlPart(qeClass, QL_ORDER_BY);
        //if (qlOrderBy != null) return qlOrderBy;
        StringBuffer qlOrderBy = null;
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            qlOrderBy = getQlPart(c, QL_ORDER_BY);
            if (qlOrderBy != null) return qlOrderBy;
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        return qlOrderBy;
    }

    //---------------------------
    public static void setQlPart(Class qeClass, String partKey, StringBuffer qlPart) {
        Map<String, StringBuffer> qeMap = qlParts.get(qeClass);
        if (qeMap == null) {
            qeMap = new HashMap<String, StringBuffer>();
            qlParts.put(qeClass, qeMap);
        }
        qeMap.put(partKey, qlPart);
    }
    public static void setQlSelect(Class qeClass, StringBuffer qlSelect) {
        setQlPart(qeClass, QL_SELECT, qlSelect);
    }

    public static void setQlFrom(Class qeClass, StringBuffer qlFrom) {
        setQlPart(qeClass, QL_FROM, qlFrom);
    }

    public static void setQlJoin(Class qeClass, StringBuffer qlJoin) {
        setQlPart(qeClass, QL_JOIN, qlJoin);
    }

    public static void setQlWhere(Class qeClass, StringBuffer qlWhere) {
        setQlPart(qeClass, QL_WHERE, qlWhere);
    }

    public static void setQlOrderBy(Class qeClass, StringBuffer qlOrderBy) {
        setQlPart(qeClass, QL_ORDER_BY, qlOrderBy);
    }

    //--------------------------- 条件字段
    public static void setFilterField(Class qeClass, String paramName, String fieldName) {
        Map<String, String> fieldMap = filterMap.get(qeClass);
        if (fieldMap == null) {
            fieldMap = new HashMap<String, String>();
            filterMap.put(qeClass, fieldMap);
        }
        fieldMap.put(paramName, fieldName);
    }

    public static void setFilterFields(Class qeClass, String[] fields) {
        Map<String, String> fieldMap = filterMap.get(qeClass);
        if (fieldMap == null) {
            fieldMap = new HashMap<String, String>();
            filterMap.put(qeClass, fieldMap);
        }

       List<String> likeList = likeFilterMap.get(qeClass);
        if (likeList == null) {
            likeList = new ArrayList<String>();
            likeFilterMap.put(qeClass, likeList);
        }
        for (int i=0; i<fields.length; i=i+2) {
            fieldMap.put(fields[i], fields[i+1]);
            if (fields[i].endsWith("_like")) {
                likeList.add(fields[i]);
            }
        }
    }

    public static String getFilterField(Class qeClass, String paramName) {
        Map<String, String> fieldMap = filterMap.get(qeClass);
        if (fieldMap == null) return null;
        else return fieldMap.get(paramName);
    }

    public static String getFilterFieldEx(Class qeClass, String paramName) {
        //String field = getFilterField(qeClass, paramName);
        //if (field != null) return field;
        String field = null;
        for(Class c = qeClass; c != AtomicEntity.class; c = c.getSuperclass()) {
            field = getFilterField(c, paramName);
            if (field != null) return field;
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        return field;
    }

    public static List<String> getLikeFields(Class qeClass) {
        List<String> list = new ArrayList<String>();
        for(Class c = qeClass; c != BasicEntity.class; c = c.getSuperclass()) {
            List likeList = likeFilterMap.get(c);
            if (likeList == null) init(c);
            likeList = likeFilterMap.get(c);
            if (likeList != null)
                list.addAll(likeList);
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        return list;
    }

    //---------------------- 实体字段到ql查询字段的映射 --------------------
    private static Map<Class, Map<String, String>> field2columnMap = new HashMap<Class, Map<String, String>>();

    /**
     * 设置实体类字段到ql查询的映射
     * @param qeClass
     * @param fieldColumns
     */
    public static void setFieldColumns(Class qeClass, String[] fieldColumns) {
        Map<String, String> field2column = field2columnMap.get(qeClass);
        if (field2column == null) {
            field2column = new HashMap<String, String>();
            field2columnMap.put(qeClass, field2column);
        }

        for (int i=0; i<fieldColumns.length; i=i+2) {
            field2column.put(fieldColumns[i], fieldColumns[i+1]);
        }
    }

    public static String getFieldColumn(Class qeClass, String fieldName) {
        Map<String, String> field2column = field2columnMap.get(qeClass);
        if (field2column == null) {
            init(qeClass);
            field2column = field2columnMap.get(qeClass);
        }
        if (field2column == null) return null;
        return field2column.get(fieldName);
    }

    public static String getFieldColumnEx(Class qeClass, String fieldName) {
        String column = null;
        for(Class c = qeClass; c != AtomicEntity.class; c = c.getSuperclass()) {
            column = getFieldColumn(c, fieldName);
            if (column != null) return column;
            if (isDirectImplTopQe(c)) {
                break;
            }
        }
        return column;
    }

    /**
     * 调用qeClass的init方法
     * @param qeClass
     */
    public static void init(Class qeClass) {
        try {
            //调用init()方法执行静态初始化
            Method m = qeClass.getMethod("init", new Class[]{});
            m.invoke(null, new Object[]{});
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
