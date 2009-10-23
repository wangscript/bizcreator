/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.hibernate.validator.NotNull;

/**
 * 关系实体: 表示两个实体间的多对多的关系, 如: 当事人关系、区域关系、产品类别之间的关系
 * @author Administrator
 */
@MappedSuperclass
public class RelationEntity extends BasicEntity implements Serializable {//
    
    @FieldInfo(name="名称", width=200)
    protected AtomicEntity entity;

    @FieldInfo(name="源角色")
    protected CodeName roleType;

    @FieldInfo(name="目标实体", isColumn=false)
    protected AtomicEntity entityTo;

    @FieldInfo(name="目标角色", isColumn=false)
    protected CodeName roleTypeTo;

    @FieldInfo(name="关系类型", description="关系类型")
    protected CodeName relationType;

    public RelationEntity(){}
    
    public RelationEntity(String id, String entityId, String entityName, 
            CodeName roleType, String entityToId, String entityToName, 
            CodeName roleTypeTo, CodeName relationType) {
        
        this.qe = true;
        this.id = id;
        createEntity(entityId, entityName);
        this.roleType = roleType;
        if (entityToId != null)
            createEntityTo(entityToId, entityToName);
        this.roleTypeTo = roleTypeTo;
        this.relationType = relationType;
    }

    public void createEntity(String entityId, String entityName) {
        if (entity == null) entity = new AtomicEntity(entityId, entityName);
    }
    
    public void createEntityTo(String entityToId, String entityToName) {
        if (entityTo == null) entityTo = new AtomicEntity(entityToId, entityToName);
    }
    
    @Transient
    public AtomicEntity getEntity() {
        return entity;
    }

    public void setEntity(AtomicEntity entity) {
        this.entity = entity;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code",column=@Column(name="role_type")),
        @AttributeOverride(name="name", column=@Column(name="role_type_name"))
    })
    public CodeName getRoleType() {
        return roleType;
    }

    public void setRoleType(CodeName roleType) {
        this.roleType = roleType;
    }

     @Transient
    public AtomicEntity getEntityTo() {
        return entityTo;
    }

    public void setEntityTo(AtomicEntity entityTo) {
        this.entityTo = entityTo;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code",column=@Column(name="role_type_to")),
        @AttributeOverride(name="name", column=@Column(name="role_type_to_name"))
    })
    public CodeName getRoleTypeTo() {
        return roleTypeTo;
    }

    public void setRoleTypeTo(CodeName roleTypeTo) {
        this.roleTypeTo = roleTypeTo;
    }

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code", column=@Column(name="relation_type")),
        @AttributeOverride(name="name", column=@Column(name="relation_type_name"))
    })
    public CodeName getRelationType() {
        return relationType;
    }

    public void setRelationType(CodeName relationType) {
        this.relationType = relationType;
    }
    
    @Override
    public String toString() {
        return entity != null ? entity.toString() : super.toString();
    }
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql select
            QueryFactory.setQlSelect(RelationEntity.class, new StringBuffer(
                "o.id, entity.id, entity.name, o.roleType, entityTo.id, entityTo.name, o.roleTypeTo, o.relationType"));
            
            //ql join
            QueryFactory.setQlJoin(RelationEntity.class,
                new StringBuffer("join o.entity entity")
                .append(" join o.entityTo entityTo")
            );
            
            QueryFactory.setFilterFields(RelationEntity.class, new String[]{
                "name_like", "entity.name",
                "entityId", "entity.id",
                "entityToId", "entityTo.id",
                "relationType", "o.relationType.code",
            });
        }
    }
    
    public final static String GROUP_KEY = "relationTypeCode";
    
    public final static Map<String, CodeName> relationTypes = new HashMap<String, CodeName>();

    /*
    public static CodeName getRelationType(String code) {
        return relationTypes.get(code);
    }*/
}
