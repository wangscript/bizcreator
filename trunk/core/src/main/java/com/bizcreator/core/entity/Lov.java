package com.bizcreator.core.entity;

import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.QueryFactory;
import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.persistence.Table;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.annotation.ServiceInfo;
import java.util.ArrayList;
import org.hibernate.validator.NotEmpty;

@Entity
@Table(name = "md_lov")
public class Lov extends BasicEntity implements Serializable {
	
    private static final long serialVersionUID = -1283578379634990649L;
    
    @FieldInfo(name="ID", width=100, isPk=true)
    protected String id;

    @FieldInfo(name="说明", description="说明")
    protected String description;

    @FieldInfo(name="项目", description="值列表项目细节", isColumn=false)
    protected List<LovDetail> details;

    public Lov() {}
    
    public Lov(String id, String name) {
        super(id, name);
    }
    
    @Id
    @Column(name = "id", length = 30)
    @Override
    public String getId() {
            return id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
    
    @NotEmpty
    @Column(name = "name", length = 60)
    @Override
    public String getName() {
        return name;
    }
    
	@Column(name = "description", length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(mappedBy="lov", cascade=CascadeType.ALL)
	public List<LovDetail> getDetails() {
		return details;
	}

	public void setDetails(List<LovDetail> details) {
		this.details = details;
	}
	
    public void addDetail(LovDetail detail) {
        if (detail == null) throw new IllegalArgumentException("Can't add a null detail to a lov");
        if (details == null) details = new ArrayList<LovDetail>();
        details.add(detail);
        detail.setLov(this);
    }
    
    public void removeDetail(LovDetail detail) {
        if (detail == null) throw new IllegalArgumentException("Can't remove a null detail from a lov");
        if (details!=null) {
            if (details.remove(detail)) {
                detail.setLov(null);
            }
        }
    }
    
	//-------------------------------- 常量 ------------------------
	public final static String LOV_ROLE_TYPE = "roleType";
	public final static String LOV_TRADE_TYPE = "tradeType";
	public final static String LOV_PROPERTY_TYPE = "propertyType";

    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql filters
            QueryFactory.setFilterFields(Lov.class, new String[]{
                "name_like", "o.name",
            });
        }
     }
}
