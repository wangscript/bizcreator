/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import java.io.Serializable;


public class BizModel implements Serializable {

    protected String id;

    //编码
    protected String code;

    //名称
    protected String name;
    
    //单据类型
    protected String docType;

    //实体类
    protected String entityClass;

    //表名
    protected String tableName;

    //主键
    protected String primaryProperty;

    //关联键
    protected String relatedProperty;

    //模型层次
    protected int level;

    //
    protected String superId;

    protected String rootId;

    protected String parentId;

    //头体关系(一对一, 一对多)
    protected String associateType;

    //是否可引用
    protected boolean refable;
    
    //是否可自定义
    protected boolean custable;

    //编码字段
    protected String codeField;

    //显示字段
    protected String displayField;

    //列表字段
    protected String listFields;

    protected String description;

    protected String helpInfo;

}
