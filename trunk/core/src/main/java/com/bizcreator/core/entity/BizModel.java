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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryProperty() {
        return primaryProperty;
    }

    public void setPrimaryProperty(String primaryProperty) {
        this.primaryProperty = primaryProperty;
    }

    public String getRelatedProperty() {
        return relatedProperty;
    }

    public void setRelatedProperty(String relatedProperty) {
        this.relatedProperty = relatedProperty;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAssociateType() {
        return associateType;
    }

    public void setAssociateType(String associateType) {
        this.associateType = associateType;
    }

    public boolean isRefable() {
        return refable;
    }

    public void setRefable(boolean refable) {
        this.refable = refable;
    }

    public boolean isCustable() {
        return custable;
    }

    public void setCustable(boolean custable) {
        this.custable = custable;
    }

    public String getCodeField() {
        return codeField;
    }

    public void setCodeField(String codeField) {
        this.codeField = codeField;
    }

    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    public String getListFields() {
        return listFields;
    }

    public void setListFields(String listFields) {
        this.listFields = listFields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(String helpInfo) {
        this.helpInfo = helpInfo;
    }

    
}
