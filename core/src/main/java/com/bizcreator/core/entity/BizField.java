/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import java.io.Serializable;

/**
 *
 * @author lgh
 */
public class BizField implements Serializable {

    protected String id;

    protected String modelId;

    protected String docType;

    protected String name;

    protected String nameTw;

    protected String nameEn;

    protected String tableName;

    protected String code; //属性名称(propertyName)

    protected String columnName;

    protected String javaType;

    protected String jdbcType;

    protected int len;

    protected int prec;

    protected int scale;

    //文本、数值、日期、引用、编号
    protected String logicType;

    //引用方式(1-基础资料、2-辅助资料、3-存货辅助属性、4-多类别、5-业务编码)
    protected int refMode;

    protected String refModelId;

    protected String refProperty;

    //引用显示方式: 模糊查询、查询全部
    protected int refDisplayMode;

    //引用过滤条件
    protected String refFilter;

    //手工录入、已有基础数据属性、通过公式得到
    protected int defMode;

    protected String defSrcProperty;

    protected String defModelId;

    protected String defProperty;

    protected String defValue;

    protected boolean nullable;

    protected String min;

    protected String max;

    protected String displayText;

    protected int displayLen;

    protected String displayFormat;

    protected String printFormat;

    protected String description;

    protected String helpInfo;


    //是否查询条件
    protected boolean searchable;

    //是否保存
    protected boolean needSave;

    //连续新增保留值的方式
    protected int valueReservedMode;

    //汇总方式(求和、平均、计数)
    protected int statMode;

    //是否预设字段
    protected boolean reserved;

    //是否主键
    protected boolean pk;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTw() {
        return nameTw;
    }

    public void setNameTw(String nameTw) {
        this.nameTw = nameTw;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getPrec() {
        return prec;
    }

    public void setPrec(int prec) {
        this.prec = prec;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getLogicType() {
        return logicType;
    }

    public void setLogicType(String logicType) {
        this.logicType = logicType;
    }

    public int getRefMode() {
        return refMode;
    }

    public void setRefMode(int refMode) {
        this.refMode = refMode;
    }

    public String getRefModelId() {
        return refModelId;
    }

    public void setRefModelId(String refModelId) {
        this.refModelId = refModelId;
    }

    public String getRefProperty() {
        return refProperty;
    }

    public void setRefProperty(String refProperty) {
        this.refProperty = refProperty;
    }

    public int getRefDisplayMode() {
        return refDisplayMode;
    }

    public void setRefDisplayMode(int refDisplayMode) {
        this.refDisplayMode = refDisplayMode;
    }

    public String getRefFilter() {
        return refFilter;
    }

    public void setRefFilter(String refFilter) {
        this.refFilter = refFilter;
    }

    public int getDefMode() {
        return defMode;
    }

    public void setDefMode(int defMode) {
        this.defMode = defMode;
    }

    public String getDefModelId() {
        return defModelId;
    }

    public void setDefModelId(String defModelId) {
        this.defModelId = defModelId;
    }

    public String getDefProperty() {
        return defProperty;
    }

    public void setDefProperty(String defProperty) {
        this.defProperty = defProperty;
    }

    public String getDefSrcProperty() {
        return defSrcProperty;
    }

    public void setDefSrcProperty(String defSrcProperty) {
        this.defSrcProperty = defSrcProperty;
    }

    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public int getDisplayLen() {
        return displayLen;
    }

    public void setDisplayLen(int displayLen) {
        this.displayLen = displayLen;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getPrintFormat() {
        return printFormat;
    }

    public void setPrintFormat(String printFormat) {
        this.printFormat = printFormat;
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

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isNeedSave() {
        return needSave;
    }

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    public int getValueReservedMode() {
        return valueReservedMode;
    }

    public void setValueReservedMode(int valueReservedMode) {
        this.valueReservedMode = valueReservedMode;
    }

    public int getStatMode() {
        return statMode;
    }

    public void setStatMode(int statMode) {
        this.statMode = statMode;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    
    
}
