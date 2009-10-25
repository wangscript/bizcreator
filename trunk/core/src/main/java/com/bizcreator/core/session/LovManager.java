package com.bizcreator.core.session;

import com.bizcreator.core.session.ServiceBase;
import java.util.List;

import javax.ejb.Local;

import com.bizcreator.core.entity.Lov;
import com.bizcreator.core.entity.LovDetail;

@Local
public interface LovManager extends ServiceBase {
    
	public final static String NAME = "lovManager";
	
	//------------------- Lov管理 --------------------------
	public List<Lov> findAllLovs();
	
	
	//-------------------- LovDetail管理 ----------------------
	public List<LovDetail> findLovDetails(String lovId);
	
	public List<LovDetail> findLovDetails(String lovId, String[] codes);
	
	public List<LovDetail> findLovDetails(String lovId, String codeList);
}
