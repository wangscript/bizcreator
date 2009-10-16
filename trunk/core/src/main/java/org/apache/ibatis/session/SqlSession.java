package org.apache.ibatis.session;

import java.sql.Connection;
import java.util.List;

public interface SqlSession {

  Object selectOne(String statement);

  Object selectOne(String statement, Object parameter);

  List selectList(String statement);

  List selectList(String statement, Object parameter);

  List selectList(String statement, Object parameter, RowBounds rowBounds);

  void select(String statement, Object parameter, ResultHandler handler);

  void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler);

  int insert(String statement);

  int insert(String statement, Object parameter);

  int update(String statement);

  int update(String statement, Object parameter);

  int delete(String statement);

  int delete(String statement, Object parameter);

  void commit();

  void commit(boolean force);

  void rollback();

  void rollback(boolean force);

  void close();

  Configuration getConfiguration();

  <T> T getMapper(Class<T> type);

  Connection getConnection();
}
