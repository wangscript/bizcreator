/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.dao.BizModelDao;
import com.bizcreator.core.session.BizModelManager;
import java.util.Map;

/**
 *
 * @author lgh
 */
public class BizModelSession extends IBatis3Base implements BizModelManager {

    public final static String NS = BizModelDao.class.getName();
    
    @Override
    public String ns() {
        return NS;
    }

    public Map findById(String id) {
        Map bizModel = (Map) getSqlSessionTemplate().selectOne(NS + ".findById", "10000");
        System.out.println(">>>biz model: " + bizModel);
        return bizModel;
    }
    
}
