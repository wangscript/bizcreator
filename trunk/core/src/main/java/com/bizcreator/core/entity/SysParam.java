package com.bizcreator.core.entity;

import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.CodeName;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.annotation.ServiceInfo;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Embedded;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

/**
 * 系统参数
 * @author Administrator
 *
 */
@Entity
@Table(name = "md_sys_param")
public class SysParam extends BasicEntity implements Serializable{
	
	@FieldInfo(name="参数类型")
	protected CodeName paramType;
	
	@FieldInfo(name="参数编码")
	protected String code;
	
	@FieldInfo(name="参数值")
	protected String paramValue;
	
	@FieldInfo(name="备注")
	protected String description;
	
    public SysParam() {
        this.paramType = CodeName.NOT_APPLICABLE;
    }
    
	@Id @GeneratedValue(generator="domainIdGen")
	@GenericGenerator(name="domainIdGen", strategy="com.bizcreator.core.hibernate.DomainIdentifierGenerator",
		parameters={
			@Parameter(name="seq", value="md_sys_param")
		}
	)
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
    
	 @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="code",column=@Column(name="param_type")),
        @AttributeOverride(name="name", column=@Column(name="param_type_name"))
    })
	public CodeName getParamType() {
		return paramType;
	}

	public void setParamType(CodeName paramType) {
		this.paramType = paramType;
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
	@Column(name = "param_value", length = 1000)
	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Column(name = "description", length = 2000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//实用方法
	public int intValue() {
		if (paramValue == null) return 0;
		else {
			try {
				return Integer.parseInt(paramValue);
			}
			catch (NumberFormatException ex) {
				return 0;
			}
		}
	}
	
	
	public double doubleValue() {
		if (paramValue == null) return 0.0d;
		else {
			try {
				return Double.parseDouble(paramValue);
			}
			catch (NumberFormatException ex) {
				return 0.0d;
			}
		}
	}
}
