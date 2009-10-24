package com.bizcreator.core.ibatis;
import org.apache.ibatis.session.SqlSession;

// see Ibatis3DaoSupport.execute
public interface SqlSessionCallback {
    public Object doInSqlSession(SqlSession session);
}
