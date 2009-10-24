package com.bizcreator.core.ibatis.example;

import com.bizcreator.core.ibatis.Ibatis3DaoSupport;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

@Transactional(isolation = org.springframework.transaction.annotation.Isolation.READ_COMMITTED, readOnly = true)
public class IbatisTestFinder extends Ibatis3DaoSupport implements TestFinder {
    public String findTest(int id) {
        SqlSession session = openSession();

        TestMapper mapper = session.getMapper(TestMapper.class);
        return mapper.selectTestById(id);
    }
}
