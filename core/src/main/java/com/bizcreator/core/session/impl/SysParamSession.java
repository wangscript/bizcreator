/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.impl.BasicSession;
import com.bizcreator.core.entity.SysParam;
import com.bizcreator.core.session.SysParamManager;
import java.util.List;
import javax.ejb.Stateless;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Stateless
@Transactional
public class SysParamSession extends BasicSession implements SysParamManager{

    public List<SysParam> findSysParams(String paramType) {
        List<SysParam> list = executeQuery("from SysParam o where o.paramType.code=?", new Object[]{paramType});
		return list;
    }
    
}
