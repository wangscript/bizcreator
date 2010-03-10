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
import java.sql.ResultSet;
import java.sql.SQLException;

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
//@Entity
//@Table(name = "biz_entity")
public class BizEntity extends BasicEntity implements Serializable, Jsonizable {

    @FieldInfo(name="编码")
    protected String code;

    @FieldInfo(name="说明")
    protected String description;

    @FieldInfo(name="帮助信息")
    protected String help_info;

    @FieldInfo(name="表名")
    protected String table_name;

    @FieldInfo(name="实体类")
    protected String entity_class;

    @FieldInfo(name="加载顺序")
    protected int load_seq;

    /**
     * 1 - Organization
     * 2 - Tenant only
     * 3 - Tenant+Organization
     * 4 - System only
     * 6 - System+Tenant
     * 7 - All
     */
    @FieldInfo(name="存取级别")
    protected CodeName access_level;

     //单据类型
    @FieldInfo(name="单据类型")
    protected CodeName doc_type;

    //主键
    @FieldInfo(name="主键字段")
    protected String primary_field;

    @FieldInfo(name="主键列")
    protected String primary_column;

    //关联键
    @FieldInfo(name="关联字段")
    protected String related_field;

    @FieldInfo(name="关联列")
    protected String related_column;

    //模型层次(顶层为0)
    @FieldInfo(name="模型层次")
    protected int level;

    //protected String superId;

    @FieldInfo(name="根类型")
    protected String root_id;

    @FieldInfo(name="父类型")
    protected String parent_id;

    //头体关系(一对一, 一对多)
    @FieldInfo(name="头体关系")
    protected CodeName associate_type;

    //是否可引用
    @FieldInfo(name="可引用")
    protected boolean is_refable;

    //是否可自定义
    @FieldInfo(name="可自定义")
    protected boolean is_custable;

    //编码字段
    @FieldInfo(name="编码字段")
    protected String code_field;

    @FieldInfo(name="编码列")
    protected String code_column;

    //显示字段
    @FieldInfo(name="显示字段")
    protected String display_field;

    @FieldInfo(name="显示列")
    protected String display_column;

    //列表字段
    @FieldInfo(name="列表字段")
    protected String list_fields;

    @FieldInfo(name="列表列")
    protected String list_columns;

    
    public BizEntity() {
        this.access_level = new CodeName("7", "All");
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
    public String getHelp_info() {
        return help_info;
    }

    public void setHelp_info(String help_info) {
        this.help_info = help_info;
    }

    @Column(name = "table_name", length = 100)
    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    @Column(name = "entity_class", length = 200)
    public String getEntity_class() {
        return entity_class;
    }

    public void setEntity_class(String entity_class) {
        this.entity_class = entity_class;
    }

    @Column(name = "load_seq")
    public int getLoad_seq() {
        return load_seq;
    }

    public void setLoad_seq(int load_seq) {
        this.load_seq = load_seq;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "access_level")),
        @AttributeOverride(name = "name", column = @Column(name = "access_level_name"))
    })
    public CodeName getAccess_level() {
        return access_level;
    }

    public void setAccess_level(CodeName access_level) {
        this.access_level = access_level;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "doc_type")),
        @AttributeOverride(name = "name", column = @Column(name = "doc_type_name"))
    })
    public CodeName getDocType() {
        return doc_type;
    }

    public void setDoc_type(CodeName doc_type) {
        this.doc_type = doc_type;
    }

    @Column(name = "primary_column", length=200)
    public String getPrimary_column() {
        return primary_column;
    }

    public void setPrimary_column(String primary_column) {
        this.primary_column = primary_column;
    }

    @Column(name = "primary_field", length=200)
    public String getPrimary_field() {
        return primary_field;
    }

    public void setPrimary_field(String primary_field) {
        this.primary_field = primary_field;
    }

    @Column(name = "related_column", length=200)
    public String getRelated_column() {
        return related_column;
    }

    public void setRelated_column(String related_column) {
        this.related_column = related_column;
    }

    @Column(name = "related_field", length=200)
    public String getRelated_field() {
        return related_field;
    }

    public void setRelated_field(String related_field) {
        this.related_field = related_field;
    }

    @Column(name = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "associate_type")),
        @AttributeOverride(name = "name", column = @Column(name = "associate_type_name"))
    })
    public CodeName getAssociate_type() {
        return associate_type;
    }

    public void setAssociate_type(CodeName associate_type) {
        this.associate_type = associate_type;
    }

    @Column(name = "is_refable")
    public boolean getIs_refable() {
        return is_refable;
    }

    public void setIs_refable(boolean is_refable) {
        this.is_refable = is_refable;
    }

    @Column(name = "is_custable")
    public boolean getIs_custable() {
        return is_custable;
    }

    public void setIs_custable(boolean is_custable) {
        this.is_custable = is_custable;
    }

    @Column(name = "code_column", length=200)
    public String getCode_column() {
        return code_column;
    }

    public void setCode_column(String code_column) {
        this.code_column = code_column;
    }

    @Column(name = "code_field", length=200)
    public String getCode_field() {
        return code_field;
    }

    public void setCode_field(String code_field) {
        this.code_field = code_field;
    }

    @Column(name = "display_column", length=200)
    public String getDisplay_column() {
        return display_column;
    }

    public void setDisplay_column(String display_column) {
        this.display_column = display_column;
    }

    @Column(name = "display_field", length=200)
    public String getDisplayField() {
        return display_field;
    }

    public void setDisplay_field(String display_field) {
        this.display_field = display_field;
    }

    @Column(name = "list_columns", length=500)
    public String getList_columns() {
        return list_columns;
    }

    public void setList_columns(String list_columns) {
        this.list_columns = list_columns;
    }

    @Column(name = "list_fields", length=500)
    public String getList_fields() {
        return list_fields;
    }

    public void setList_fields(String list_fields) {
        this.list_fields = list_fields;
    }


    @Override
    public Object fromJson(BizJsonObject json) {

        super.fromJson(json);

        this.code = json.getAsString("code");
        this.name = json.getAsString("name");
        this.description = json.getAsString("description");
        this.help_info = json.getAsString("help_info");
        this.table_name = json.getAsString("table_name");
        this.entity_class = json.getAsString("entity_class");
        //todo
        this.load_seq = json.getAsInt("load_seq");
        this.access_level = new CodeName(json.getAsString("access_level"), json.getAsString("access_level_name"));
        this.doc_type = new CodeName(json.getAsString("doc_type"), json.getAsString("doc_type_name"));
        this.primary_field = json.getAsString("primary_field");
        this.primary_column = json.getAsString("primary_column");
        this.related_field = json.getAsString("related_field");
        this.related_column = json.getAsString("related_column");
        this.level = json.getAsInt("level");
        this.associate_type = new CodeName(json.getAsString("associate_type"), json.getAsString("associate_type_name"));

        this.root_id = json.getAsString("root_id");

        return this;
    }

    public BizEntity fromRs(ResultSet rs) throws SQLException {
    	this.code = rs.getString("code");
		this.name = rs.getString("name");
		this.description = rs.getString("description");
		this.help_info = rs.getString("help_info");
		this.table_name = rs.getString("table_name");
		this.entity_class = rs.getString("entity_class");
        
		
        //todo
		this.load_seq = rs.getInt("load_seq");
        this.access_level = new CodeName(rs.getString("access_level"), rs.getString("access_level_name"));
        this.doc_type = new CodeName(rs.getString("doc_type"), rs.getString("doc_type_name"));
        this.primary_field = rs.getString("primary_field");
        this.primary_column = rs.getString("primary_column");
        this.related_field = rs.getString("related_field");
        this.related_column = rs.getString("related_column");
        this.level = rs.getInt("level");
        this.associate_type = new CodeName(rs.getString("associate_type"), rs.getString("associate_type_name"));

        this.root_id = rs.getString("root_id");
        
        return this;
    }
    
    @Override
    public JsonObject toJson() {

        JsonObject json = super.toJson();
        json.addProperty("code", this.code);
        json.addProperty("name", this.name);
        json.addProperty("description", this.description);
        json.addProperty("help_info", this.help_info);
        json.addProperty("table_name", this.table_name);
        json.addProperty("entity_class", this.entity_class);
        json.addProperty("load_seq", this.load_seq);
        json.addProperty("access_level", this.access_level.getCode());
        json.addProperty("access_level_name", this.access_level.getName());

        json.addProperty("doc_type", this.doc_type.getCode());
        json.addProperty("doc_type_name", this.doc_type.getName());

        json.addProperty("primary_field", this.primary_field);
        json.addProperty("primary_column", this.primary_column);
        json.addProperty("related_field", this.related_field);
        json.addProperty("related_column", this.related_column);

        json.addProperty("level", this.level);

        json.addProperty("associate_type", this.associate_type.getCode());
        json.addProperty("associate_type_name", this.associate_type.getName());

        json.addProperty("root_id", this.root_id);
        

        return json;
    }
    
    
}
