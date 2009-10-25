package com.bizcreator.core.entity;

import com.bizcreator.core.entity.BasicEntity;
import com.bizcreator.core.QueryFactory;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.json.JSONConverter;
import com.bizcreator.core.json.Jsonizable;
import net.sf.json.JSONObject;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;

/**
 * 序列号：
 * 1. 维护各表的主键id值；
 * 2. 不同的域服务器分别保存一套列表；
 * 3. 各域服务器的sequence数据应尽量同步；
 * 
 * @author <a href="mailto:rhino03142000@yahoo.com">luoguanhua</a>
 * @version $Revision: 1.8 $
 */
@Entity
@Table(name = "md_sequence")
public class Sequence extends BasicEntity implements Serializable, Jsonizable {

    private static final long serialVersionUID = 7584154495605000815L;

    @FieldInfo(name = "域", description = "所属域", isSearch = true)
    protected String domain;

    @FieldInfo(name = "序列值", description = "序列值", isSearch = true)
    protected int nextValue;

    @FieldInfo(name = "备注", description = "备注", isSearch = true)
    protected String description;

    public Sequence() {
    }

    public Sequence(String domain, String name, int nextValue, String description) {
        super();
        this.domain = domain;
        this.name = name;
        this.nextValue = nextValue;
        this.description = description;
    }

    @Id
    @GeneratedValue(generator = "domainIdGen")
    @GenericGenerator(name = "domainIdGen", strategy = "com.bizcreator.core.hibernate.DomainIdentifierGenerator",
    parameters = {
        @Parameter(name = "seq", value = "md_sequence")
    })
    @Column(name = "id", length = 30)
    @Override
    public String getId() {
        return id;
    }

    @NotEmpty
    @Column(name = "name", length = 100)
    @Override
    public String getName() {
        return name;
    }

    @NotEmpty
    @Column(name = "domain", length = 60)
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @NotNull
    @Column(name = "next_value")
    public int getNextValue() {
        return nextValue;
    }

    public void setNextValue(int nextValue) {
        this.nextValue = nextValue;
    }

    @Column(name = "description", length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return domain + "." + name;
    }

    public JSONObject toJSON() {
        return JSONConverter.toJSON(this, new String[]{"id", "name", "domain", "nextValue", "description"});
    }

    public Object fromJSON(JSONObject json) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql join
            //ql filters
            QueryFactory.setFilterFields(Sequence.class, new String[]{
                        "name_like", "o.name",
                        "domain", "o.domain",});
        }
    }

    
}
