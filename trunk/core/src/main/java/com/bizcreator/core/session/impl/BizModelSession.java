/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.ibatis.Ibatis3DaoSupport;
import com.bizcreator.core.session.BizModelManager;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author lgh
 */
public class BizModelSession extends Ibatis3DaoSupport implements BizModelManager {

    public Map findById(String id) {
        SqlSession session = openSession();
        try {
            Map bizModel = (Map) session.selectOne("core.BizModel.findById", "10000");
            System.out.println(">>>biz model: " + bizModel);
            return bizModel;
        } finally {
            session.close();
        }
    }
}
