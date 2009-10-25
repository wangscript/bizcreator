package com.bizcreator.core.entity;

import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.entity.CodeName;
import com.bizcreator.core.QueryFactory;
import com.bizcreator.core.annotation.ColumnOrder;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.annotation.ServiceInfo;
import javax.persistence.Embedded;
import javax.persistence.Transient;

/**
 * 应该分层次
 * @author luoguanhua
 *
 */
@ColumnOrder({"codeName.code", "codeName.name", "description"})
@Entity
@Table(name = "md_lov_detail")
public class LovDetail extends BasicEntity implements Serializable {
    
    private static final long serialVersionUID = 9015055311791473604L;

    @FieldInfo(name="代码名称", expandable=true)
    protected CodeName codeName;

    @FieldInfo(name="说明", description="说明")
    protected String description;

    @FieldInfo(name="父级代码", description="父级")
    protected String parentCode;

    @FieldInfo(name="lov", description="lov", isColumn=false)
    protected Lov lov;
	
    public LovDetail(){
        codeName = new CodeName();
    }
    
    public LovDetail(String id, String name) {
        super(id, name);
    }
    
    
	@Id @GeneratedValue(generator="domainIdGen")
	@GenericGenerator(name="domainIdGen", strategy="com.bizcreator.core.hibernate.DomainIdentifierGenerator",
		parameters={
			@Parameter(name="seq", value="md_menu")
		}
	)
	@Column(name = "id", length = 30)
    @Override
	public String getId() {
		return id;
	}
    
    @Embedded
    public CodeName getCodeName() {
        return codeName;
    }

    public void setCodeName(CodeName codeName) {
        this.codeName = codeName;
    }
    
    @Transient
    public String getCode() {
        return codeName.getCode();
    }

    public void setCode(String code) {
        codeName.setCode(code);
    }
    
    @Transient
    @Override
    public String getName() {
        return codeName.getName();
    }
    
    @Override
    public void setName(String name) {
        codeName.setName(name);
    }
    
	@Column(name = "description", length = 255)
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
		
	@Column(name = "parent_code", length = 30)
	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="lov_id")
	public Lov getLov() {
		return lov;
	}

	public void setLov(Lov lov) {
		this.lov = lov;
	}
	
    @Override
    public String toString() {
        return codeName.toString();
    }
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql filters
            QueryFactory.setFilterFields(LovDetail.class, new String[]{
                "code_like", "o.codeName.code",
                "name_like", "o.codeName.name",
                "lovId", "o.lov.id",
            });
        }
     }
}
