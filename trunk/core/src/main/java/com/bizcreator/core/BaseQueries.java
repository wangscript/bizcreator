/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理命名的查询串
 * @author Administrator
 */
public class BaseQueries implements NamedQueries {

    protected static Map<String, String> namedQueries = new HashMap<String, String>();

    public final static String findMenuModelQes = "findMenuModelQes";
    public final static String findProcessDefs = "findProcessDefs";
    public final static String findProcessInsts = "findProcessInsts";

    public BaseQueries() {
        namedQueries.put(findMenuModelQes,
                new StringBuffer(" select new com.rhinofield.base.entity.MenuModelQe(o.id, o.name, ")
                .append(" o.code, o.menuType, o.expression, o.description, o.seq) ")
                .append(" from MenuModel o where o.subsystem.id=:subsystemId ")
                .toString());
        namedQueries.put(findProcessDefs, new StringBuffer("select new com.rhinofield.base.jbpm.model.ProcessDef(")
                .append("o.id, o.name, o.description, o.version, o.isTerminationImplicit)")
                .append(" from org.jbpm.graph.def.ProcessDefinition o order by o.name")
                .toString());
        namedQueries.put(findProcessInsts, new StringBuffer("select new com.rhinofield.base.jbpm.model.ProcessInst(")
                .append("o.id, o.key, o.start, o.end, o.processDefinition.id, o.version, o.rootToken.id, )")
                .append("o.superProcessToken.id, o.suspended) ")
                .append(" from org.jbpm.graph.exe.ProcessInstance o where o.processDefinition.id=:processId")
                .toString());
    }

    public String getQlString(String name) {
        return namedQueries.get(name);
    }
}
