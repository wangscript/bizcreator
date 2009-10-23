/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.data.BeanModel;
import com.bizcreator.core.annotation.FieldInfo;
import com.bizcreator.core.Describable;
import com.bizcreator.core.QueryFactory;
import java.io.Serializable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * 所有实体的父类, 只包含id和name字段, 同时实现相关的功能
 * @author Administrator
 */
@MappedSuperclass
public class AtomicEntity extends BeanModel implements Serializable, Describable {//
    
    @FieldInfo(name="ID", isColumn=false, width=0, isPk=true)
    protected String id;
    
    @FieldInfo(name="名称", isSearch=true, width=200, isDisplay=true)
    protected String name;

    //是否query entity
    protected boolean qe = false;
    protected AtomicEntity assigned = null;
    
    //在调用save(...)方法时, 可能会根据ServiceInfo标注, 获取服务, 并可能形成调用循环
    //通过该设置标志以避免这种情况发生
    protected boolean serviced;
    
    public AtomicEntity() {}
    
    public AtomicEntity(String id, String name) {
        this.id = id;
        this.name = name;
        this.qe = true;
    }
    
    @Transient
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        //String oldValue = this.name;
        this.name = name;
        
        //通知属性变化
        //firePropertyChange("name", oldValue, name);
    }

    @Transient
    public boolean isQe() {
        return qe;
    }

    public void setQe(boolean qe) {
        this.qe = qe;
    }

    @Transient
    public AtomicEntity getAssigned() {
        return assigned;
    }

    public void setAssigned(AtomicEntity assigned) {
        this.assigned = assigned;
    }

    @Transient
    public boolean isServiced() {
        return serviced;
    }

    public void setServiced(boolean serviced) {
        this.serviced = serviced;
    }

    //------------------------ 实体的回调方法 ----------------------------------
    /**
     * 新建保存之前调用
     */
    public void beforePersist() {}
    
    /**
     * 新建保存之后调用
     */
    public void afterPersist(){}
    
    public void beforeMerge() {}
    
    public void afterMerge() {}
    
    public void beforeSave() {}
    
    public void afterSave() {}
    
    public void beforeRemove(){}
    
    public void afterRemove() {}
    
    /**
     * 参考jmatter实现title()方法, 可以在子类中根据实际情况, 重载该方法
     * @return
     */
    public String title() {
        return toString();
    }
    
    public static void init() {
        String endPoint = (String) System.getProperties().get("end_point");
        if ("server".equals(endPoint)) {
            //ql default entity class
            //ql select
            QueryFactory.setQlSelect(AtomicEntity.class, new StringBuffer("o.id, o.name"));
            
            //ql filters
            
        }
    }
    
    @Override
    public int hashCode() {
        if (id != null) return id.hashCode();
        else if (name != null) return name.hashCode();
        else return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof AtomicEntity) {
            AtomicEntity ae = (AtomicEntity) obj;
            if ((ae.getId() != null && ae.getId().equals(id)) ||
                    (ae.getId() == null && id == null && ae.getName() != null && ae.getName().equals(name))) {
                return true;
            }
        }
        return false;
    } //	equals
    
    @Override
    public String toString() {
        if (name != null) return name;
        else if (id != null) return id;
        else return "";
    }
}
