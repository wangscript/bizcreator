package com.bizcreator.core.entity;

import com.bizcreator.core.QueryFactory;
import javax.persistence.MappedSuperclass;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.json.BizJsonObject;
import com.bizcreator.core.json.Jsonizable;
import com.google.gson.JsonObject;

import java.util.Date;
import javax.persistence.Column;

@MappedSuperclass
public class BasicEntity extends AtomicEntity implements java.io.Serializable, Jsonizable {

    //系统委托人(公司)
    @FieldInfo(name = "公司", isColumn = false)
    protected String client_id;
    //部门(机构)
    @FieldInfo(name = "机构", isColumn = false)
    protected String org_id;
    //激活字段
    @FieldInfo(name = "激活?", isColumn = false)
    protected boolean is_active;
    //创建时间
    @FieldInfo(name = "创建时间", isColumn = false)
    protected Date created;
    //创建人
    @FieldInfo(name = "创建人", isColumn = false)
    protected String created_by;
    //更新时间
    @FieldInfo(name = "更新时间", isColumn = false)
    protected Date updated;
    //更新人
    @FieldInfo(name = "更新人", isColumn = false)
    protected String updated_by;

    public BasicEntity() {
    }

    public BasicEntity(String id, String name) {
        super(id, name);
    }

    public BasicEntity(String id, String name, String client_id,
            String org_id, boolean is_active, Date created, String created_by,
            Date updated, String updated_by) {
        this(id, name);
        this.client_id = client_id;
        this.org_id = org_id;
        this.is_active = is_active;
        this.created = created;
        this.created_by = created_by;
        this.updated = updated;
        this.updated_by = updated_by;
    }

    @Column(name = "client_id", length = 30)
    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    @Column(name = "org_id", length = 30)
    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    @Column(name = "is_active")
    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "created_by", length = 30)
    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @Column(name = "updated")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name = "updated_by", length = 30)
    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public Object fromJson(BizJsonObject json) {

        this.id = json.getAsString("id");
        this.client_id = json.getAsString("client_id");
        this.org_id = json.getAsString("org_id");
        this.is_active = json.getAsBoolean("is_active");
        this.created = json.getAsDate("created");
        this.created_by = json.getAsString("created_by");
        this.updated = json.getAsDate("updated");
        this.updated_by = json.getAsString("updated_by");

        return this;
    }

    public JsonObject toJson() {

        JsonObject json = new JsonObject();

        json.addProperty("id", this.id);
        json.addProperty("client_id", this.client_id);
        json.addProperty("org_id", this.org_id);
        json.addProperty("is_active", this.is_active);
        if (this.created != null) {
            json.addProperty("created", this.created.getTime());
        }
        json.addProperty("created_by", this.created_by);
        if (this.updated != null) {
            json.addProperty("updated", this.updated.getTime());
        }
        json.addProperty("updated_by", this.updated_by);

        return json;
    }

    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql select
            QueryFactory.setQlSelect(BasicEntity.class, new StringBuffer(
                    "o.client_id, o.org_id, o.is_active, o.created, o.created_by, o.updated, o.updated_by"));

            //ql filters
            QueryFactory.setFilterFields(BasicEntity.class, new String[]{
                        "client_id", "o.client_id",
                        "org_id", "o.org_id",
                        "is_active", "o.is_active",
                        "created_ge", "o.created",
                        "created_le", "o.created",
                        "updated_ge", "o.updated",
                        "updated_le", "o.updated",
                        "created_by", "o.created_by",
                        "updated_by", "o.updated_by",});
        }
    }
}
