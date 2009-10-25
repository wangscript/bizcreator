package com.bizcreator.core.entity;


import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.annotation.ColumnOrder;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import com.bizcreator.core.annotation.FieldInfo;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

@ColumnOrder({"code", "name", "menuType", "expression", "seq", "iconPath"})
@Entity
@Table(name = "md_menu")
public class MenuModel extends BasicEntity implements Serializable {//

    public final static int MENU_BAR = 0;	//菜单条
    public final static int MENU = 1;		//菜单
    public final static int MENU_ITEM = 2;	//菜单项
    public final static int RADIO = 3;
    public final static int CHECK_BOX = 4;
    
    @FieldInfo(name = "编码", description = "编码")
    protected String code;

    @FieldInfo(name = "菜单类型")
    protected CodeName menuType;

    @FieldInfo(name = "模块表达式", description = "模块表达式可以是模块类名")
    protected String expression;

    @FieldInfo(name = "说明", description = "说明")
    protected String description;

    @FieldInfo(name = "顺序", description = "顺序")
    protected int seq;

    @FieldInfo(name = "汇总级", description = "汇总级")
    protected boolean summary;

    @FieldInfo(name = "图标")
    protected String iconPath;

    @FieldInfo(name = "上级", description = "上级", isColumn = false)
    protected MenuModel parent;

    @FieldInfo(name = "子菜单", isColumn = false)
    protected List<MenuModel> children;

    @FieldInfo(name = "所属子系统", isColumn = false)
    protected Subsystem subsystem;

    @FieldInfo(name = "功能", isColumn = false)
    protected List<Function> functions;
    
    protected boolean separated;

    public MenuModel() {
        this.menuType = new CodeName();
    }

    public MenuModel(String id, String name) {
        super(id, name);
    }

    public MenuModel(String id, String name, String code,
            CodeName menuType, String expression, String description,
            int seq, String iconPath, String parentId, String parentName) {
        this(id, name);
        this.code = code;
        this.menuType = menuType;
        this.expression = expression;
        this.description = description;
        this.seq = seq;
        this.iconPath = iconPath;
        if (parentId != null) {
            this.parent = new MenuModel(parentId, parentName);
        }
    }

    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "md_menu")
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

    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "code", column = @Column(name = "menu_type")),
        @AttributeOverride(name = "name", column = @Column(name = "menu_type_name"))
    })
    public CodeName getMenuType() {
        return menuType;
    }

    public void setMenuType(CodeName menuType) {
        this.menuType = menuType;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "is_summary")
    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    @Column(name = "seq")
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Column(name = "icon_path")
    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    @Column(name = "expression")
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    public MenuModel getParent() {
        return parent;
    }

    public void setParent(MenuModel parent) {
        this.parent = parent;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subsystem_id")
    public Subsystem getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    @Column(name = "separated")
    public boolean isSeparated() {
        return separated;
    }

    public void setSeparated(boolean separated) {
        this.separated = separated;
    }

    //------------------- function relatives ----------------
    @OneToMany(mappedBy = "menuModel", cascade = CascadeType.ALL)
    public List<Function> getFunctions() {
        return functions;
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public void addFunction(Function function) {
        //System.out.println(">>>functions: " + functions);
        if (function == null) {
            throw new IllegalArgumentException("Can't add a null function to a menu");
        }
        if (functions == null) {
            functions = new ArrayList<Function>();
        }
        functions.add(function);
        function.setMenuModel(this);
    }

    public void removeFunction(Function function) {
        if (function == null) {
            throw new IllegalArgumentException("Can't remove a null function from a menu");
        }
        if (functions != null) {
            if (functions.remove(function)) {
                function.setMenuModel(null);
            }
        }
    }

    public void removeAllFunctions() {
        if (functions != null) {
            for (Function func : functions) {
                removeFunction(func);
            }
        }
    }

    //----------------- children relatives -----------------
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("seq")
    public List<MenuModel> getChildren() {
        return children;
    }

    public void setChildren(List<MenuModel> children) {
        this.children = children;
    }

    public void addChild(MenuModel child) {
        if (child == null) {
            throw new IllegalArgumentException("Can't add a null child to a parent");
        }
        if (children == null) {
            children = new ArrayList<MenuModel>();
        }
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(MenuModel child) {
        if (child == null) {
            throw new IllegalArgumentException("Can't remove a null child from a parent");
        }
        if (children != null) {
            if (children.remove(child)) {
                child.setParent(null);
            }
        }
    }

    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class

            //ql select
            QueryFactory.setQlSelect(MenuModel.class, new StringBuffer(
                    "o.id, o.name, o.code, o.menuType, ").append(" o.expression, o.description, o.seq, o.iconPath,").append(" parent.id, parent.name"));

            //ql join
            QueryFactory.setQlJoin(MenuModel.class, new StringBuffer(
                    "left join o.parent parent").append(" join o.subsystem subsystem"));

            //ql where
            QueryFactory.setQlOrderBy(MenuModel.class,
                    new StringBuffer("o.seq"));

            //ql filters
            QueryFactory.setFilterFields(MenuModel.class, new String[]{
                        "code_like", "o.code",
                        "name_like", "o.name",
                        "subsystemId", "subsystem.id",
                        "menuTypeCode", "o.menuType.code",
                        "parentId", "parent.id",});
        }
    }
}
