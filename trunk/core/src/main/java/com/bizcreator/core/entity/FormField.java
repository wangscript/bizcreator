/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.entity;

import com.bizcreator.core.entity.MenuModel;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotEmpty;

/**
 * 记录表单子段, 以便进行权限管理: 只读、掩藏等
 * @author Administrator
 */
@Entity
@Table(name = "md_form_field")
public class FormField extends BasicEntity implements Serializable {

    @FieldInfo(name = "代码", description = "代码", width = 100, isSearch = true)
    protected String code;
    @FieldInfo(name = "说明", description = "说明", width = 300)

    protected String description;
    @FieldInfo(name = "所属菜单", isColumn = false)
    protected MenuModel menuModel;

    public FormField() {
    }

    public FormField(String id, String name) {
        super(id, name);
    }

    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.rhinofield.base.hibernate.id.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "md_form_field")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql filters
            QueryFactory.setFilterFields(FormField.class, new String[]{
                        "code_like", "o.code",
                        "menuId", "o.menuModel.id",});
        }
    }
}
