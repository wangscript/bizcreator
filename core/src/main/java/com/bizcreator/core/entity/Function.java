package com.bizcreator.core.entity;

import com.bizcreator.core.entity.MenuModel;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.QueryFactory;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bizcreator.core.annotation.AsLov;
import com.bizcreator.core.annotation.FieldInfo;
import javax.persistence.FetchType;
import org.hibernate.validator.NotEmpty;

/**
 * 功能:
 * 1. 每个模块都包含多个不同的功能；
 * 2. 各模块的功能应通过元数据在模块中定义，当模块被部署时，自动导入数据库中；
 * @author <a href="mailto:rhino03142000@yahoo.com">luoguanhua</a>
 * @version $Revision: 1.5 $
 *
 */

@Entity
@Table(name = "md_function")
public class Function extends BasicEntity implements Serializable {
	
    private static final long serialVersionUID = 3265529591320069781L;
            
	@FieldInfo(name="代码", description="代码", width=100, isSearch=true)
	protected String code;
	
	@FieldInfo(name="说明", description="说明", width=300)
	protected String description;
	
	@FieldInfo(name="类型", description="功能类型", width=60)
	@AsLov(lovId="lovFunctionType")
	protected int functionType;
	
	@FieldInfo(name="表达式", description="表达式", width=250)
	protected String expression;
	
	@FieldInfo(name="图标路径", description="图标路径")
	protected String iconPath;
	
	@FieldInfo(name="所属菜单", isColumn=false)
	protected MenuModel menuModel;
	
    
    public Function() {}
    
    public Function(String id, String name) {
        super(id, name);
    }
    
    
	@Id @GeneratedValue(generator="domainIdGen")
	@GenericGenerator(name="domainIdGen", strategy="com.rhinofield.base.hibernate.id.DomainIdentifierGenerator",
		parameters={
			@Parameter(name="seq", value="md_function")
		}
	)
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
	
	@Column(name = "expression", length = 255)
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	@Column(name = "icon_path", length = 255)
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@Column(name = "function_type")
	public int getFunctionType() {
		return functionType;
	}

	public void setFunctionType(int functionType) {
		this.functionType = functionType; 
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="menu_id")
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
            QueryFactory.setFilterFields(Function.class, new String[]{
                "code_like", "o.code",
                "name_like", "o.name",
                "functionType", "o.functionType",
                "menuId", "o.menuModel.id",
            });
        }
     }
}
