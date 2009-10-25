/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.CodeName;
import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.annotation.FieldInfo;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class StatusLog extends BasicEntity implements java.io.Serializable {////
	
    @FieldInfo(name="操作时间")
    protected Date handleTime;

    @FieldInfo(name="操作类型")
    protected CodeName op;

    @FieldInfo(name="状态")
    protected CodeName status;

    @FieldInfo(name="操作员")
    protected String handler;

    @FieldInfo(name="备注")
    protected String description;

    @FieldInfo(name="所属主体", isColumn=false)
    protected StatusEntity master;
    
    public StatusLog(){}
    
    public StatusLog(Date handleTime, CodeName op, CodeName status,
            String handler, String description, String masterId, String masterName) {
            this.handleTime = handleTime;
            this.op = op;
            this.status = status;
            this.handler = handler;
            this.description = description;
            this.master = new StatusEntity(masterId, masterName);
    }
    
    @Column(name="handle_time")
	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code",column=@Column(name="op")),
        @AttributeOverride(name="name", column=@Column(name="op_name"))
    })
    public CodeName getOp() {
        return op;
    }

    public void setOp(CodeName op) {
        this.op = op;
    }

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

	@Column(name="handler", length=30)
	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Transient
    public StatusEntity getMaster() {
        return master;
    }

    public void setMaster(StatusEntity master) {
        this.master = master;
    }
    
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //default entity class
            //QueryFactory.setDefaultFromClass(StatusLog.class, StatusLog.class);
            
            //ql select
            QueryFactory.setQlSelect(StatusLog.class,
                new StringBuffer(" o.handleTime, o.op, o.status, o.handler, o.description,")
                .append(" master.id, master.docNo")
            );
            
            //ql join
            QueryFactory.setQlJoin(StatusLog.class,
                new StringBuffer(" join o.master master")
            );
            
            //ql where
            QueryFactory.setQlWhere(StatusLog.class,
                new StringBuffer(" master.id=:masterId")
            );
        }
    }
}