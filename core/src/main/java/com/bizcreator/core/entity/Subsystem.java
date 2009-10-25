package com.bizcreator.core.entity;

import com.bizcreator.core.entity.MenuModel;
import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.QueryFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.annotation.ServiceInfo;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import org.hibernate.validator.NotEmpty;

/**
 * 子系统：
 * 
 * @author <a href="mailto:rhino03142000@yahoo.com">luoguanhua</a>
 * @version $Revision: 1.6 $
 *
 */
@ServiceInfo(name="moduleManager")
@Entity
@Table(name = "md_subsystem")
public class Subsystem extends BasicEntity implements Serializable {
	
    private static final long serialVersionUID = -8979548432314375283L;
            
	/**
	 * 代码:
	 * 1. 代码应保持唯一性；
	 * 2. 与模块代码组合起来，可以直接在命令行输入并启动模块；
	 */
	@FieldInfo(name="代码", description="子系统代码", isSearch=true)
	protected String code;
	
	@FieldInfo(name="备注", description="备注")
	protected String description;
	
	@FieldInfo(name="图标路径", description="图标路径")
	protected String iconPath;
	
	@FieldInfo(name="顺序", description="顺序号")
	protected int seq;
	
	@FieldInfo(name="菜单模块", description="菜单模块", isColumn=false)
	protected List<MenuModel> menus;
	
    @Override
	@Id @GeneratedValue(generator="domainIdGen")
	@GenericGenerator(name="domainIdGen", strategy="com.bizcreator.core.hibernate.DomainIdentifierGenerator",
		parameters={
			@Parameter(name="seq", value="md_subsystem")
		}
	)
	@Column(name = "id", length = 30)
	public String getId() {
		return id;
	}
    
    @NotEmpty
	@Column(name = "code", length = 30)
	public String getCode() {
		return code;
	}
    
    @NotEmpty
    @Column(name = "name", length = 60)
    @Override
    public String getName() {
        return name;
    }
     
	public void setCode(String code) {
		this.code = code;
	}
    
	@Column(name = "icon_path", length = 255)
	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	@Column(name = "seq")
	public int getSeq() {
		return seq;
	}
	
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	@Column(name = "description", length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    @OneToMany(mappedBy="subsystem", cascade=CascadeType.ALL)
    public List<MenuModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuModel> menus) {
        this.menus = menus;
    }
    
    public void addMenu(MenuModel menu) {
        if (menu == null) throw new IllegalArgumentException("Can't add a null menu to a subsystem");
        if (menus == null) menus = new ArrayList<MenuModel>();
        menus.add(menu);
        menu.setSubsystem(this);
    }
    
    public void removeMenu(MenuModel menu) {
        if (menu == null) throw new IllegalArgumentException("Can't remove a null menu from a subsystem");
        if (menus!=null) {
            if (menus.remove(menu)) {
                menu.setSubsystem(null);
            }
        }
    }
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql join
            //ql filters
            QueryFactory.setFilterFields(Subsystem.class, new String[]{
                "code_like", "o.code",
            });
        }
     }
}
