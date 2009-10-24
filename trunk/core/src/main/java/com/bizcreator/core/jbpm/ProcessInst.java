/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.jbpm;

import com.bizcreator.core.annotation.FieldInfo;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Administrator
 */
public class ProcessInst implements Serializable {
    
    @FieldInfo(name="ID")
    long id = 0;
    
    @FieldInfo(name="Key")
    protected String key = null;
    
    @FieldInfo(name="开始时间")
    protected Date start = null;
    
    @FieldInfo(name="结束时间")
    protected Date end = null;
    
    @FieldInfo(name="流程Id")
    protected long processId;
    
    @FieldInfo(name="版本")
    int version = 0;
    
    @FieldInfo(name="Root token")
    protected long rootTokenId;
    
    @FieldInfo(name="Super token")
    protected long superProcessTokenId;
    
    @FieldInfo(name="暂停?")
    protected boolean isSuspended = false;
    
    public final String STATUS_RUNNING = "Running";
    public final String STATUS_START = "Start";
    public final String STATUS_END = "End";
    
    public ProcessInst(long id, String key, Date start, Date end, long processId, 
             int version, long rootTokenId, long superProcessTokenId, boolean isSuspended) {
        this.id = id;
        this.key = key;
        this.start = start;
        this.end = end;
        this.processId = processId;
        this.version = version;
        this.rootTokenId = rootTokenId;
        this.superProcessTokenId = superProcessTokenId;
        this.isSuspended = isSuspended;
    }
    
    public long getId() {
        return id;
    }
    
    /** a unique business key */
    public String getKey() {
        return key;
    }
    
    /** set the unique business key */
    public void setKey(String key) {
        this.key = key;
    }
    
    public Date getStart() {
        return start;
    }
    
    public void setStart(Date start) {
        this.start = start;
    }
    
    public Date getEnd() {
        return end;
    }
    
    public void setEnd(Date end) {
        this.end = end;
    }
    
    public long getProcessId() {
        return processId;
    }
    
    public void setProcessId(long processId) {
        this.processId = processId;
    }
    
    public int getVersion() {
        return version;
    }
    
    public void setVersion(int version) {
        this.version = version;
    }
    
    public long getRootTokenId() {
        return rootTokenId;
    }
    
    public void setRootTokenId(long rootTokenId) {
        this.rootTokenId = rootTokenId;
    }
    
    public long getSuperProcessTokenId() {
        return superProcessTokenId;
    }

    public void setSuperProcessTokenId(long superProcessTokenId) {
        this.superProcessTokenId = superProcessTokenId;
    }
  
    public boolean isSuspended() {
        return isSuspended;
    }
  
    public String getStatus() {
        if (end != null) {
            return STATUS_END;
        }
        else if (!isSuspended) {
            return STATUS_RUNNING;
        }
        else {
            return STATUS_START;
        }
    }
}
