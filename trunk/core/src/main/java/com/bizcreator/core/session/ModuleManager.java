package com.bizcreator.core.session;

import java.util.List;

import javax.ejb.Local;

import com.bizcreator.core.entity.Function;
import com.bizcreator.core.entity.MenuModel;
import com.bizcreator.core.entity.Subsystem;

@Local
public interface ModuleManager extends ServiceBase {

    public final static String NAME = "moduleManager";

    public List<Subsystem> findAllSubsystems();

    public Subsystem findSubsystemByCode(String code);

    //---------------- 查找菜单子节点 --------------------
    public List<MenuModel> findSysMenus(String subsystemId);

    public List<MenuModel> findMenuChildren(String menuId);

    public MenuModel findMenuModel(String subsystemId, String expression);

    //---------------- for function -----------------
    public List<Function> findFunctions(String menuId);

    public void removeFunctions(String menuId);

    //-----------------------------------------------
    public void testCreate();

    public void testRemove();
}
