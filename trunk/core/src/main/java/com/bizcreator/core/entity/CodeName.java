/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.data.BeanModel;
import java.io.Serializable;
import com.bizcreator.core.annotation.FieldInfo;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.hibernate.validator.NotEmpty;

/**
 * <代码, 名称>对, 用于封装所选的Lov代码
 * @author Administrator
 */
@Embeddable
public class CodeName extends BeanModel implements Serializable {
    
    @FieldInfo(name="编码")
    public String code;
    
    @FieldInfo(name="名称")
    public String name;

    public CodeName(){}
    
    public CodeName(String code, String name) {
        this.code = code;
        this.name = name;
    }
    
    @NotEmpty
    @Column(name = "code", length = 30)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotEmpty
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final static CodeName NOT_APPLICABLE = new CodeName("N/A", "--无--");
    
    @Override
    public String toString() {
        if (name != null) return name;
        else if (code != null) return code;
        else return "";
    }
    
    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CodeName other = (CodeName) obj;
        if (this.code == null || !this.code.equals(other.code)) {
            return false;
        }
        /*
        if (this.name == null || !this.name.equals(other.name)) {
            return false;
        }*/
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.code != null ? this.code.hashCode() : 0);
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    public final static CodeName NULL = new CodeName("", "<空>");
    
    
}