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
    protected String clientId;
    //部门(机构)
    @FieldInfo(name = "机构", isColumn = false)
    protected String orgId;
    //激活字段
    @FieldInfo(name = "激活?", isColumn = false)
    protected boolean isActive;
    //创建时间
    @FieldInfo(name = "创建时间", isColumn = false)
    protected Date created;
    //创建人
    @FieldInfo(name = "创建人", isColumn = false)
    protected String createdBy;
    //更新时间
    @FieldInfo(name = "更新时间", isColumn = false)
    protected Date updated;
    //更新人
    @FieldInfo(name = "更新人", isColumn = false)
    protected String updatedBy;

    public BasicEntity() {
    }

    public BasicEntity(String id, String name) {
        super(id, name);
    }

    public BasicEntity(String id, String name, String clientId,
            String orgId, boolean isActive, Date created, String createdBy,
            Date updated, String updatedBy) {
        this(id, name);
        this.clientId = clientId;
        this.orgId = orgId;
        this.isActive = isActive;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    @Column(name = "client_id", length = 30)
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Column(name = "org_id", length = 30)
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    @Column(name = "is_active")
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "created_by", length = 30)
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "updated")
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Column(name = "updated_by", length = 30)
    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object fromJson(BizJsonObject json) {

        this.id = json.getAsString("id");
        this.clientId = json.getAsString("clientId");
        this.orgId = json.getAsString("orgId");
        this.isActive = json.getAsBoolean("isActive");
        this.created = json.getAsDate("created");
        this.createdBy = json.getAsString("createdBy");
        this.updated = json.getAsDate("updated");
        this.updatedBy = json.getAsString("updatedBy");

        return this;
    }

    public JsonObject toJson() {

        JsonObject json = new JsonObject();

        json.addProperty("id", this.id);
        json.addProperty("clientId", this.clientId);
        json.addProperty("orgId", this.orgId);
        json.addProperty("isActive", this.isActive);
        if (this.created != null) {
            json.addProperty("created", this.created.getTime());
        }
        json.addProperty("createdBy", this.createdBy);
        if (this.updated != null) {
            json.addProperty("updated", this.updated.getTime());
        }
        json.addProperty("updatedBy", this.updatedBy);

        return json;
    }

    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql select
            QueryFactory.setQlSelect(BasicEntity.class, new StringBuffer(
                    "o.clientId, o.orgId, o.isActive, o.created, o.createdBy, o.updated, o.updatedBy"));

            //ql filters
            QueryFactory.setFilterFields(BasicEntity.class, new String[]{
                        "clientId", "o.clientId",
                        "orgId", "o.orgId",
                        "isActive", "o.isActive",
                        "created_ge", "o.created",
                        "created_le", "o.created",
                        "updated_ge", "o.updated",
                        "updated_le", "o.updated",
                        "createdBy", "o.createdBy",
                        "updatedBy", "o.updatedBy",});
        }
    }
}
