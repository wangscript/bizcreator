/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.entity.Function;
import com.bizcreator.core.entity.MenuModel;
import com.bizcreator.core.entity.Subsystem;
import com.bizcreator.core.session.ModuleManager;
import java.util.List;
import javax.ejb.Stateless;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Stateless
@Transactional
public class ModuleSession extends BasicSession implements ModuleManager {

    public List<Subsystem> findAllSubsystems() {
        return executeQuery("from Subsystem o order by o.seq");
    }

    public Subsystem findSubsystemByCode(String code) {
        return (Subsystem) getSingleResult("from Subsystem o where o.code=?", new Object[] {code});
    }
    
    //--------------- for menu model ---------------------
    public List<MenuModel> findSysMenus(String subsystemId) {

        StringBuffer sb= new StringBuffer("from MenuModel o where o.parent is null ")
            .append(" and o.subsystem.id=?")
            .append(" order by o.seq");
        
        return executeQuery(sb.toString(), new Object[]{subsystemId});

        /*
        List list = queryEntities(MenuModel.class, MenuModel.class, "parent is null", 
            new Object[]{"subsystemId", subsystemId});
        return list; */
    }

    public List<MenuModel> findMenuChildren(String menuId) {
        

        List<MenuModel> list = executeNpQuery("from MenuModel o where o.parent.id = :menuId order by o.seq",
                new Object[]{"menuId", menuId});
		
		return list;
        /*
        List list = queryEntities(MenuModel.class, MenuModel.class, "parent is null", 
            new Object[]{"parentId", menuId});
        return list;
         */
    }
    
    public MenuModel findMenuModel(String subsystemId, String expression) {
        MenuModel menuModel = (MenuModel) getNpSingleResult(
                "from MenuModel o where o.subsystem.id=:subsystemId and o.expression=:expression", 
                new Object[]{"subsystemId", subsystemId, "expression", expression}
        );
        return menuModel;
    }
    
    // ----------------- for function ------------------------
    public List<Function> findFunctions(String menuId) {
        List<Function> list = executeNpQuery("from Function o where o.menuModel.id = :menuId ",
            new Object[]{"menuId", menuId});
        return list;
    }
    
    public void removeFunctions(String menuId) {
        executeNpUpdate("delete from Function o where o.menuModel.id = :menuId ",
            new Object[]{"menuId", menuId});
    }
    
    

    
    
    //-------------------- for test -----------------------
     public void testCreate() {
            Subsystem subsystem = new Subsystem();
            subsystem.setCode("bus");
            subsystem.setName("客运管理");
            //add menu
            MenuModel menu = new MenuModel();
            menu.setCode("MTicketSale");
            menu.setName("售票");
            menu.setExpression("com.rhinofield.tms.bus.TicketSale");
            subsystem.addMenu(menu);
            
            Function func = new Function();
            func.setCode("create");
            func.setName("创建售票实体");
            func.setDescription("创建售票实体");
            menu.addFunction(func);
            
            Function func1 = new Function();
            func1.setCode("update");
            func1.setName("更新售票实体");
            func1.setDescription("更新售票实体");
            menu.addFunction(func1);
            
            MenuModel menu1 = new MenuModel();
            menu1.setCode("MTicketCheck");
            menu1.setName("检票");
            menu1.setExpression("com.rhinofield.tms.bus.TicketCheck");
            subsystem.addMenu(menu1);
            
            create(subsystem);
            
        }
     
     public void testRemove() {
            Subsystem subsystem = findSubsystemByCode("bus");
            
            if (subsystem != null) {
                /*
                List<MenuModel> menus = subsystem.getMenus();
                if (menus != null) {
                    for (MenuModel m : menus) {
                        System.out.println(">>>menu: " + m.getName());
                    }
                }
             */
                remove(subsystem);
            }
     }
     
     //-------------------------- old implements -------------------
     /*
	public List<Module> findAllModules() {
		List<Module> list = em.createQuery("from Module o ").getResultList();
		return list;
	}
	
	public List<Function> findFunctions(String moduleId) {
		List<Function> list = em.createQuery("from Function o where module.id=:moduleId")
		.setParameter("moduleId", moduleId)
		.getResultList();
		return list;
	}
	
	public Module findModuleByCode(String code) {
		Module module = (Module) em.createQuery("from Module o where o.code=:code")
		.setParameter("code", code)
		.getSingleResult();
		return module;
	}
	
	public List<Module> findModules(Subsystem subsystem) {
		merge(subsystem);
		return subsystem.getModules();
        return null;
	}
	
	public void addModule(Subsystem subsystem, Module module) {
		merge(subsystem);
		subsystem.addModule(module);
	}
	
	public void addModules(Subsystem subsystem, Module[] modules) {
		merge(subsystem);
		for (Module module : modules) {
			subsystem.addModule(module);
		}
	}
	
	public void removeModule(Subsystem subsystem, Module module) {
		merge(subsystem);
		subsystem.removeModule(module);
	}
	
	public void removeModules(Subsystem subsystem, Module[] modules) {
		merge(subsystem);
		for (Module module : modules) {
			subsystem.removeModule(module);
		}
	}
	
	//------------------------ 菜单管理 ---------------------------
    
	public List<MenuModel> findSysMenus(String subsystemId) {
		StringBuffer sb= new StringBuffer("from MenuModel o where o.parentMenu is null ")
		.append(" and o.subsystem.id=:subsystemId")
		.append(" order by o.seq");
		List<MenuModel> list = em.createQuery(sb.toString())
		.setParameter("subsystemId", subsystemId)
		.getResultList();
		return list;
	}
	
	public List<MenuModel> findMenuChildren(String menuId) {
		List<MenuModel> list = em.createQuery("from MenuModel o where o.parentMenu.id = :menuId order by o.seq")
			.setParameter("menuId", menuId)
			.getResultList();
		
		return list;
	}

    public Subsystem findSubsystemByCode(String code) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void testCreate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void testRemove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
      */
}
