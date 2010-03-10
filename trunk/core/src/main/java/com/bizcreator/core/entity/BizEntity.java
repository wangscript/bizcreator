/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.json.BizJsonObject;
import com.bizcreator.core.json.Jsonizable;
import com.google.gson.JsonObject;
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
@Table(name = "biz_entity")
public class BizEntity extends BasicEntity implements Serializable, Jsonizable {

    @FieldInfo(name="编码")
    protected String code;

    @FieldInfo(name="说明")
    protected String description;

    @FieldInfo(name="帮助信息")
    protected String helpInfo;

    @FieldInfo(name="表名")
    protected String tableName;

    @FieldInfo(name="实体类")
    protected String entityClass;

    @FieldInfo(name="加载顺序")
    protected int loadSeq;

    /**
     * 1 - Organization
     * 2 - Tenant only
     * 3 - Tenant+Organization
     * 4 - System only
     * 6 - System+Tenant
     * 7 - All
     */
    @FieldInfo(name="存取级别")
    protected CodeName accessLevel;

     //单据类型
    @FieldInfo(name="单据类型")
    protected CodeName docType;

    //主键
    @FieldInfo(name="主键字段")
    protected String primaryField;

    @FieldInfo(name="主键列")
    protected String primaryColumn;

    //关联键
    @FieldInfo(name="关联字段")
    protected String relatedField;

    @FieldInfo(name="关联列")
    protected String relatedColumn;

    //模型层次(顶层为0)
    @FieldInfo(name="模型层次")
    protected int level;

    //protected String superId;

    @FieldInfo(name="根类型")
    protected BizEntity root;

    @FieldInfo(name="父类型")
    protected BizEntity parent;

    //头体关系(一对一, 一对多)
    @FieldInfo(name="头体关系")
    protected CodeName associateType;

    //是否可引用
    @FieldInfo(name="可引用")
    protected boolean refable;

    //是否可自定义
    @FieldInfo(name="可自定义")
    protected boolean custable;

    //编码字段
    @FieldInfo(name="编码字段")
    protected String codeField;

    @FieldInfo(name="编码列")
    protected String codeColumn;

    //显示字段
    @FieldInfo(name="显示字段")
    protected String displayField;

    @FieldInfo(name="显示列")
    protected String displayColumn;

    //列表字段
    @FieldInfo(name="列表字段")
    protected String listFields;

    @FieldInfo(name="列表列")
    protected String listColumns;

    public BizEntity() {
        this.accessLevel = new CodeName("7", "All");
    }

    public BizEntity(String id, String name) {
        super(id, name);
    }

    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "biz_entity")
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

    @Column(name = "help_info", length = 2000)
    public String getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(String helpInfo) {
        this.helpInfo = helpInfo;
    }

    @Column(name = "table_name", length = 100)
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Column(name = "entity_class", length = 200)
    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    @Column(name = "load_seq")
    public int getLoadSeq() {
        return loadSeq;
    }

    public void setLoadSeq(int loadSeq) {
        this.loadSeq = loadSeq;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "access_level")),
        @AttributeOverride(name = "name", column = @Column(name = "access_level_name"))
    })
    public CodeName getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(CodeName accessLevel) {
        this.accessLevel = accessLevel;
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

    @Column(name = "primary_column", length=200)
    public String getPrimaryColumn() {
        return primaryColumn;
    }

    public void setPrimaryColumn(String primaryColumn) {
        this.primaryColumn = primaryColumn;
    }

    @Column(name = "primary_field", length=200)
    public String getPrimaryField() {
        return primaryField;
    }

    public void setPrimaryField(String primaryField) {
        this.primaryField = primaryField;
    }

    @Column(name = "related_column", length=200)
    public String getRelatedColumn() {
        return relatedColumn;
    }

    public void setRelatedColumn(String relatedColumn) {
        this.relatedColumn = relatedColumn;
    }

    @Column(name = "related_field", length=200)
    public String getRelatedField() {
        return relatedField;
    }

    public void setRelatedField(String relatedField) {
        this.relatedField = relatedField;
    }

    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="root_id")
    public BizEntity getRoot() {
        return root;
    }

    public void setRoot(BizEntity root) {
        this.root = root;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="parent_id")
    public BizEntity getParent() {
        return parent;
    }

    public void setParent(BizEntity parent) {
        this.parent = parent;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "associate_type")),
        @AttributeOverride(name = "name", column = @Column(name = "associate_type_name"))
    })
    public CodeName getAssociateType() {
        return associateType;
    }

    public void setAssociateType(CodeName associateType) {
        this.associateType = associateType;
    }

    @Column(name = "is_refable")
    public boolean isRefable() {
        return refable;
    }

    public void setRefable(boolean refable) {
        this.refable = refable;
    }

    @Column(name = "is_custable")
    public boolean isCustable() {
        return custable;
    }

    public void setCustable(boolean custable) {
        this.custable = custable;
    }

    @Column(name = "code_column", length=200)
    public String getCodeColumn() {
        return codeColumn;
    }

    public void setCodeColumn(String codeColumn) {
        this.codeColumn = codeColumn;
    }

    @Column(name = "code_field", length=200)
    public String getCodeField() {
        return codeField;
    }

    public void setCodeField(String codeField) {
        this.codeField = codeField;
    }

    @Column(name = "display_column", length=200)
    public String getDisplayColumn() {
        return displayColumn;
    }

    public void setDisplayColumn(String displayColumn) {
        this.displayColumn = displayColumn;
    }

    @Column(name = "display_field", length=200)
    public String getDisplayField() {
        return displayField;
    }

    public void setDisplayField(String displayField) {
        this.displayField = displayField;
    }

    @Column(name = "list_columns", length=500)
    public String getListColumns() {
        return listColumns;
    }

    public void setListColumns(String listColumns) {
        this.listColumns = listColumns;
    }

    @Column(name = "list_fields", length=500)
    public String getListFields() {
        return listFields;
    }

    public void setListFields(String listFields) {
        this.listFields = listFields;
    }


    @Override
    public Object fromJson(BizJsonObject json) {

        super.fromJson(json);

        this.code = json.getAsString("code");
        this.name = json.getAsString("name");
        this.description = json.getAsString("description");
        this.helpInfo = json.getAsString("helpInfo");
        this.tableName = json.getAsString("tableName");
        this.entityClass = json.getAsString("entityClass");
        //todo
        this.loadSeq = json.getAsInt("loadSeq");
        this.accessLevel = new CodeName(json.getAsString("accessLevel"), json.getAsString("accessLevelName"));
        this.docType = new CodeName(json.getAsString("docType"), json.getAsString("docTypeName"));
        this.primaryField = json.getAsString("primaryField");
        this.primaryColumn = json.getAsString("primaryColumn");
        this.relatedField = json.getAsString("relatedField");
        this.relatedColumn = json.getAsString("relatedColumn");
        this.level = json.getAsInt("level");
        this.associateType = new CodeName(json.getAsString("associateType"), json.getAsString("associateTypeName"));

        this.root = new BizEntity(json.getAsString("root.id"), json.getAsString("root.name"));

        return this;
    }

    @Override
    public JsonObject toJson() {

        JsonObject json = super.toJson();
        json.addProperty("code", this.code);
        json.addProperty("name", this.name);
        json.addProperty("description", this.description);
        json.addProperty("helpInfo", this.helpInfo);
        json.addProperty("tableName", this.tableName);
        json.addProperty("entityClass", this.entityClass);
        json.addProperty("loadSeq", this.loadSeq);
        json.addProperty("accessLevel", this.accessLevel.getCode());
        json.addProperty("accessLevelName", this.accessLevel.getName());

        json.addProperty("docType", this.docType.getCode());
        json.addProperty("docTypeName", this.docType.getName());

        json.addProperty("primaryField", this.primaryField);
        json.addProperty("primaryColumn", this.primaryColumn);
        json.addProperty("relatedField", this.relatedField);
        json.addProperty("relatedColumn", this.relatedColumn);

        json.addProperty("level", this.level);

        json.addProperty("associateType", this.associateType.getCode());
        json.addProperty("associateTypeName", this.associateType.getName());

        json.addProperty("root.id", this.root.getId());
        json.addProperty("root.name", this.root.getName());

        return json;
    }
}
