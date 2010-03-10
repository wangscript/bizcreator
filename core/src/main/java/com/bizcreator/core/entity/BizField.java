/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.entity;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.json.Jsonizable;
import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotEmpty;

/**
 *
 * @author lgh
 */
@Entity
@Table(name = "biz_field")
public class BizField extends BasicEntity implements Serializable, Jsonizable {

    @FieldInfo(name = "编码")
    protected String code; //属性名称(propertyName)

    @FieldInfo(name="说明")
    protected String description;

    @FieldInfo(name="帮助信息")
    protected String helpInfo;

    @FieldInfo(name = "业务实体")
    protected BizEntity bizEntity;

    @FieldInfo(name = "单据类型")
    protected CodeName docType;

    @FieldInfo(name = "繁体名称")
    protected String nameTw;

    @FieldInfo(name = "英文名称")
    protected String nameEn;

    @FieldInfo(name = "所属表名")
    protected String tableName;

    @FieldInfo(name = "列名")
    protected String columnName;

    @FieldInfo(name = "java类型")
    protected String javaType;

    @FieldInfo(name = "jdbc类型")
    protected String jdbcType;

    @FieldInfo(name = "长度")
    protected int len;

    @FieldInfo(name = "精度")
    protected int prec;

     @FieldInfo(name="小数位数")
    protected int scale;

    //文本、数值、日期、引用、编号
    @FieldInfo(name="逻辑类型")
    protected CodeName logicType;

    //引用方式(1-基础资料、2-辅助资料、3-存货辅助属性、4-多类别、5-业务编码)
    //赋值来源
    @FieldInfo(name="引用方式")
    protected CodeName refMode;

    @FieldInfo(name="引用类型")
    protected BizEntity refEntity;

    @FieldInfo(name="引用字段")
    protected String refField;

    @FieldInfo(name="引用列")
    protected String refColumn;

    //引用显示方式: 模糊查询、查询全部
    @FieldInfo(name="引用显示方式")
    protected CodeName refDisplayMode;

    //引用过滤条件
    @FieldInfo(name="引用过滤条件")
    protected String refFilter;

    //手工录入、已有基础数据属性、通过公式得到
    @FieldInfo(name="默认值来源")
    protected CodeName defMode;

    @FieldInfo(name="来源字段")
    protected String defSrcField;

    @FieldInfo(name="来源列")
    protected String defSrcColumn;

    @FieldInfo(name="来源实体")
    protected BizEntity defEntity;

    @FieldInfo(name="取值字段")
    protected String defField;

    @FieldInfo(name="取值列")
    protected String defColumn;

    @FieldInfo(name="默认值")
    protected String defValue;

    @FieldInfo(name="必录?")
    protected boolean required;

    @FieldInfo(name="最小值")
    protected String valueMin;

    @FieldInfo(name="最大值")
    protected String valueMax;

    @FieldInfo(name="显示文本")
    protected String displayText;

    @FieldInfo(name="显示长度")
    protected int displayLen;

    @FieldInfo(name="显示格式")
    protected String displayFormat;

    @FieldInfo(name="打印格式")
    protected String printFormat;

    //是否查询条件
    @FieldInfo(name="查询条件?")
    protected boolean searchable;

    //是否保存
    @FieldInfo(name="是否保存")
    protected boolean needSave;

    //连续新增保留值的方式
    @FieldInfo(name="值保留方式")
    protected CodeName reserveMode;

    //汇总方式(求和、平均、计数)
    @FieldInfo(name="汇总方式")
    protected CodeName statMode;

    @FieldInfo(name="系统预设?")
    protected boolean reserved;

    @FieldInfo(name="主键?")
    protected boolean pk;

    @FieldInfo(name="必录规则")
    protected String requiredRule;

    @FieldInfo(name="有效性规则")
    protected String validRule;

    @FieldInfo(name="只读规则")
    protected String readOnlyRule;

    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "biz_field")
    })
    @Column(name = "id", length = 30)
    @Override
    public String getId() {
        return id;
    }

    @NotEmpty
    @Column(name = "name", length = 60)
    @Override
    public String getName() {
        return name;
    }

    @NotEmpty
    @Column(name = "code", length = 30)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "help_info", length = 1000)
    public String getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(String helpInfo) {
        this.helpInfo = helpInfo;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id")
    public BizEntity getBizEntity() {
        return bizEntity;
    }

    public void setBizEntity(BizEntity bizEntity) {
        this.bizEntity = bizEntity;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "doc_type")),
        @AttributeOverride(name = "name", column = @Column(name = "doc_type_name"))
    })
    public CodeName getDocType() {
        return docType;
    }

    public void setDocType(CodeName docType) {
        this.docType = docType;
    }

    @Column(name = "name_en", length = 100)
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Column(name = "name_tw", length = 100)
    public String getNameTw() {
        return nameTw;
    }

    public void setNameTw(String nameTw) {
        this.nameTw = nameTw;
    }

    @Column(name = "table_name", length = 100)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Column(name = "column_name", length = 100)
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Column(name = "java_type", length = 30)
    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Column(name = "jdbc_type", length = 30)
    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    @Column(name = "len")
    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    @Column(name = "prec")
    public int getPrec() {
        return prec;
    }

    public void setPrec(int prec) {
        this.prec = prec;
    }

    @Column(name = "scale")
    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "logic_type")),
        @AttributeOverride(name = "name", column = @Column(name = "logic_type_name"))
    })
    public CodeName getLogicType() {
        return logicType;
    }

    public void setLogicType(CodeName logicType) {
        this.logicType = logicType;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "ref_mode")),
        @AttributeOverride(name = "name", column = @Column(name = "ref_mode_name"))
    })
    public CodeName getRefMode() {
        return refMode;
    }

    public void setRefMode(CodeName refMode) {
        this.refMode = refMode;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ref_entity_id")
    public BizEntity getRefEntity() {
        return refEntity;
    }

    public void setRefEntity(BizEntity refEntity) {
        this.refEntity = refEntity;
    }

    @Column(name = "ref_field", length = 100)
    public String getRefField() {
        return refField;
    }

    public void setRefField(String refField) {
        this.refField = refField;
    }

    @Column(name = "ref_column", length = 100)
    public String getRefColumn() {
        return refColumn;
    }

    public void setRefColumn(String refColumn) {
        this.refColumn = refColumn;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "ref_display_mode")),
        @AttributeOverride(name = "name", column = @Column(name = "ref_display_mode_name"))
    })
    public CodeName getRefDisplayMode() {
        return refDisplayMode;
    }

    public void setRefDisplayMode(CodeName refDisplayMode) {
        this.refDisplayMode = refDisplayMode;
    }

    @Column(name = "ref_filter", length = 255)
    public String getRefFilter() {
        return refFilter;
    }

    public void setRefFilter(String refFilter) {
        this.refFilter = refFilter;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "def_mode")),
        @AttributeOverride(name = "name", column = @Column(name = "def_mode_name"))
    })
    public CodeName getDefMode() {
        return defMode;
    }

    public void setDefMode(CodeName defMode) {
        this.defMode = defMode;
    }

    @Column(name = "def_src_column", length = 100)
    public String getDefSrcColumn() {
        return defSrcColumn;
    }

    public void setDefSrcColumn(String defSrcColumn) {
        this.defSrcColumn = defSrcColumn;
    }

    @Column(name = "def_src_field", length = 100)
    public String getDefSrcField() {
        return defSrcField;
    }

    public void setDefSrcField(String defSrcField) {
        this.defSrcField = defSrcField;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="def_entity_id")
    public BizEntity getDefEntity() {
        return defEntity;
    }

    public void setDefEntity(BizEntity defEntity) {
        this.defEntity = defEntity;
    }

    @Column(name = "def_field", length = 100)
    public String getDefField() {
        return defField;
    }

    public void setDefField(String defField) {
        this.defField = defField;
    }

    @Column(name = "def_column", length = 100)
    public String getDefColumn() {
        return defColumn;
    }

    public void setDefColumn(String defColumn) {
        this.defColumn = defColumn;
    }

    @Column(name = "def_value", length = 200)
    public String getDefValue() {
        return defValue;
    }

    public void setDefValue(String defValue) {
        this.defValue = defValue;
    }

    @Column(name = "is_required")
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Column(name = "value_max", length = 100)
    public String getValueMax() {
        return valueMax;
    }

    public void setValueMax(String valueMax) {
        this.valueMax = valueMax;
    }

    @Column(name = "value_min", length = 100)
    public String getValueMin() {
        return valueMin;
    }

    public void setValueMin(String valueMin) {
        this.valueMin = valueMin;
    }

    @Column(name = "display_format", length = 200)
    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    @Column(name = "display_len")
    public int getDisplayLen() {
        return displayLen;
    }

    public void setDisplayLen(int displayLen) {
        this.displayLen = displayLen;
    }

    @Column(name = "display_text", length = 100)
    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @Column(name = "print_format", length = 100)
    public String getPrintFormat() {
        return printFormat;
    }

    public void setPrintFormat(String printFormat) {
        this.printFormat = printFormat;
    }

    @Column(name = "need_save")
    public boolean isNeedSave() {
        return needSave;
    }

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    @Column(name = "is_pk")
    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "reserve_mode")),
        @AttributeOverride(name = "name", column = @Column(name = "reserve_mode_name"))
    })
    public CodeName getReserveMode() {
        return reserveMode;
    }

    public void setReserveMode(CodeName reserveMode) {
        this.reserveMode = reserveMode;
    }

    @Column(name = "is_reserved")
    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    @Column(name = "is_searchable")
    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "stat_mode")),
        @AttributeOverride(name = "name", column = @Column(name = "stat_mode_name"))
    })
    public CodeName getStatMode() {
        return statMode;
    }

    public void setStatMode(CodeName statMode) {
        this.statMode = statMode;
    }

    @Column(name="read_only_rule", length=200)
    public String getReadOnlyRule() {
        return readOnlyRule;
    }

    public void setReadOnlyRule(String readOnlyRule) {
        this.readOnlyRule = readOnlyRule;
    }

    @Column(name="required_rule", length=200)
    public String getRequiredRule() {
        return requiredRule;
    }

    public void setRequiredRule(String requiredRule) {
        this.requiredRule = requiredRule;
    }

    @Column(name="valid_rule", length=200)
    public String getValidRule() {
        return validRule;
    }

    public void setValidRule(String validRule) {
        this.validRule = validRule;
    }

}
