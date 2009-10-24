/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.jbpm;

import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class BasicElement implements Serializable {
    
    @FieldInfo(name="ID", description="ID")
    protected long id = 0;
    
    @FieldInfo(name="名称")
    protected String name = null;
    
    @FieldInfo(name="说明")
    protected String description = null;
    
    
    public BasicElement(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    
    // getters and setters //////////////////////////////////////////////////////
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
