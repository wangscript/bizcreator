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

/*
 typeId
tableName
colName
code
typeNo
row
name
dataType
len
dec
min
max
defValue
required
format
printFormat
refTypeId
refColName
refDisplay(引用显示方式: 模糊查询、查询全部)
refMode
refFilter
Fsave(是否保存)
连续新增保留值
FSearch(是否查询条件)
Fsum
Fsys
FieldId
defTypeId
defColName
defMode(手工录入、已有基础数据属性、通过公式得到)
defSrc
usedEditable
 */
    
}
