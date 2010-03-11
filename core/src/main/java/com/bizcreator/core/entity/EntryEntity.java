/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 *
 * @author lgh
 */
@MappedSuperclass
public class EntryEntity extends BasicEntity implements Serializable {

    @FieldInfo(name="主体id", isColumn=false)
    protected AtomicEntity master;

    @FieldInfo(name="汇总行")
    protected boolean summary;

    public EntryEntity() {
        super();
    }

    public EntryEntity(String id, String name) {
        super(id, name);
    }

    public EntryEntity(String id, String name, String client_id,
        String org_id, boolean is_active, Date created, String created_by,
        Date updated, String updated_by, AtomicEntity master, boolean summary) {

        super(id, name, client_id, org_id, is_active, created, created_by,
                updated, updated_by);
        this.master = master;
        this.summary = summary;
    }

    @Transient
    public AtomicEntity getMaster() {
        return master;
    }

    public void setMaster(AtomicEntity master) {
        this.master = master;
    }

    @Column(name="is_summary")
    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }
}
