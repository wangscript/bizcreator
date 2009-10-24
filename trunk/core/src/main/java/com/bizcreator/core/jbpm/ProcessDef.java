/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.jbpm;

import com.bizcreator.core.annotation.FieldInfo;

/**
 * 该实体为了查询列表使用
 * @author Administrator
 */
public class ProcessDef extends BasicElement implements java.io.Serializable {

    @FieldInfo(name="版本")
    protected int version = -1;
    
    @FieldInfo(name="Termination Implicit?")
    protected boolean isTerminationImplicit = false;

    public ProcessDef(long id, String name, String description, int version, boolean isTerm) {
        super(id, name, description);
        this.version = version;
        this.isTerminationImplicit = isTerm;
    }
    
    // getters and setters //////////////////////////////////////////////////////
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isTerminationImplicit() {
        return isTerminationImplicit;
    }

    public void setTerminationImplicit(boolean isTerminationImplicit) {
        this.isTerminationImplicit = isTerminationImplicit;
    }
}
