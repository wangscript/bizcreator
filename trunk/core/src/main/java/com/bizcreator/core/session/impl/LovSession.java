/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.impl.BasicSession;
import com.bizcreator.core.entity.Lov;
import com.bizcreator.core.entity.LovDetail;
import com.bizcreator.core.session.LovManager;
import com.bizcreator.util.StringUtil;
import java.util.List;
import javax.ejb.Stateless;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Stateless
@Transactional
public class LovSession extends BasicSession implements LovManager {

    public List<Lov> findAllLovs() {
        return executeQuery("from Lov o ");
    }

    public List<LovDetail> findLovDetails(String lovId) {
        return executeQuery("from LovDetail o where o.lov.id = ? ", new Object[]{lovId});
    }

    public List<LovDetail> findLovDetails(String lovId, String[] codes) {
        StringBuffer sb = new StringBuffer("from LovDetail o where o.pk.lovId=?");
		if (codes != null && codes.length>0) {
			sb.append(" and (o.pk.code='" + codes[0] +"'");
			for (int i=1; i<codes.length; i++) {
				sb.append(" or o.pk.code='" + codes[i] + "'");
			}
			sb.append(")");
		}
        return executeQuery(sb.toString(), new Object[]{lovId});
    }

    public List<LovDetail> findLovDetails(String lovId, String codeList) {
        List<String> codes = StringUtil.split(codeList, ",");
		String[] aCodes = new String[codes.size()];
		aCodes = codes.toArray(aCodes);
		
		return findLovDetails(lovId, aCodes);
    }
    
}