/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core;

import com.bizcreator.core.entity.BizModel;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 *
 * @author lgh
 */
public class IBatisConfig {

    public static void main(String[] args) throws IOException {
        String resource = "ibatis-config-core.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

        SqlSession session = sessionFactory.openSession();
        try {
            Map bizModel = (Map) session.selectOne("core.BizModel.findById", "10000");
            System.out.println(">>>biz model: " + bizModel);
        } finally {
            session.close();
        }
    }
}
