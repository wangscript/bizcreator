package com.bizcreator.core.session;

import com.bizcreator.core.session.ServiceBase;
import java.util.List;

import javax.ejb.Local;
import com.bizcreator.core.entity.SysParam;

@Local
public interface SysParamManager extends ServiceBase {
	
    public final static String NAME = "sysParamManager";
    
	public List<SysParam> findSysParams(String paramType);
	
}
