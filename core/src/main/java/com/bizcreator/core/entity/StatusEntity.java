/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import org.hibernate.validator.NotNull;

/**
 * 具有状态的实体
 * @author lgh
 */
@MappedSuperclass
public class StatusEntity extends BasicEntity implements Serializable {
    
    @FieldInfo(name="状态")
    protected CodeName status;
    
    public StatusEntity() {
        status = STATUS_DR;
    }
    
    public StatusEntity(String id, String name) {
        super(id, name);
    }
    
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code",column=@Column(name="status")),
        @AttributeOverride(name="name", column=@Column(name="status_name"))
    })
    public CodeName getStatus() {
            return status;
    }

    public void setStatus(CodeName status) {
            this.status = status;
    }
    
    /**
     * 新建状态：子类应覆盖该方法以提供具体的实现
     * @return
     */
    public StatusLog newStatus() {
        return new StatusLog();
    }
    
    public final static CodeName OP_CREATE = new CodeName("create", "新建计划");
	public final static CodeName OP_CONFIRM = new CodeName("confirm", "确认计划");
	public final static CodeName OP_AUDIT = new CodeName("audit", "审核计划");
	public final static CodeName OP_RESTORE = new CodeName("restore", "状态还原");

	public final static CodeName STATUS_DR = new CodeName("DR", "新建");
	public final static CodeName STATUS_CO = new CodeName("CO", "确认");
	public final static CodeName STATUS_AP = new CodeName("AP", "审核通过");
	public final static CodeName STATUS_NP = new CodeName("NP", "审核未通过");
}