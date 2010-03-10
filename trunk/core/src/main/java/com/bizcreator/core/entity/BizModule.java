/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import javax.persistence.Column;
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

@Entity
@Table(name = "biz_module")
public class BizModule extends BasicEntity implements Serializable {

    @FieldInfo(name = "编码")
    protected String code;

    //业务实体
    @FieldInfo(name = "业务实体")
    protected BizEntity bizEntity;

    //模块类
    @FieldInfo(name = "模块类")
    protected String modulePath;

    //界面类型(Form、List、Query、Other)
    @FieldInfo(name = "界面类型")
    protected String uiType;

    @FieldInfo(name = "默认模块?")
    protected boolean defaults;

    @FieldInfo(name = "说明")
    protected String description;


    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "biz_module")
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entity_id")
    public BizEntity getBizEntity() {
        return bizEntity;
    }

    public void setBizEntity(BizEntity bizEntity) {
        this.bizEntity = bizEntity;
    }

    @Column(name = "module_path", length=200)
    public String getModulePath() {
        return modulePath;
    }

    public void setModulePath(String modulePath) {
        this.modulePath = modulePath;
    }

    @Column(name = "ui_type", length=20)
    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    @Column(name = "is_defaults")
    public boolean isDefaults() {
        return defaults;
    }

    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
