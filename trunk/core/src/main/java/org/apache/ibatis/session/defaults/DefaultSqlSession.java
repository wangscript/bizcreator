package org.apache.ibatis.session.defaults;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

  private Configuration configuration;
  private Executor executor;

  private boolean autoCommit;
  private boolean dirty;

  public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit) {
    this.configuration = configuration;
    this.executor = executor;
    this.autoCommit = autoCommit;
    this.dirty = false;
  }

  public Object selectOne(String statement) {
    return selectOne(statement, null);
  }

  public Object selectOne(String statement, Object parameter) {
    // Popular vote was to return null on 0 results and throw exception on too many.
    List list = selectList(statement, parameter);
    if (list.size() == 1) {
      return list.get(0);
    } else if (list.size() > 1) {
      throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
    } else {
      return null;
    }
  }

  public List selectList(String statement) {
    return selectList(statement, null);
  }

  public List selectList(String statement, Object parameter) {
    return selectList(statement, parameter, RowBounds.DEFAULT);
  }

  public List selectList(String statement, Object parameter, RowBounds rowBounds) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
    }
  }

  public void select(String statement, Object parameter, ResultHandler handler) {
    select(statement, parameter, RowBounds.DEFAULT, handler);
  }

  public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
    try {
      MappedStatement ms = configuration.getMappedStatement(statement);
      executor.query(ms, wrapCollection(parameter), rowBounds, handler);
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
    }
  }

  public int insert(String statement) {
    return insert(statement, null);
  }

  public int insert(String statement, Object parameter) {
    return update(statement, parameter);
  }

  public int update(String statement) {
    return update(statement, null);
  }

  public int update(String statement, Object parameter) {
    try {
      dirty = true;
      MappedStatement ms = configuration.getMappedStatement(statement);
      return executor.update(ms, wrapCollection(parameter));
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
    }
  }

  public int delete(String statement) {
    return update(statement, null);
  }

  public int delete(String statement, Object parameter) {
    return update(statement, wrapCollection(parameter));
  }

  public void commit() {
    commit(false);
  }

  public void commit(boolean force) {
    try {
      executor.commit(isCommitOrRollbackRequired(force));
      dirty = false;
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error committing transaction.  Cause: " + e, e);
    }
  }

  public void rollback() {
    rollback(false);
  }

  public void rollback(boolean force) {
    try {
      executor.rollback(isCommitOrRollbackRequired(force));
      dirty = false;
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error rolling back transaction.  Cause: " + e, e);
    }
  }

  public void close() {
    try {
      try {
        rollback(false);
      } finally {
        executor.close();
      }
    } catch (Exception e) {
      throw ExceptionFactory.wrapException("Error closing transaction.  Cause: " + e, e);
    }
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  public <T> T getMapper(Class<T> type) {
    return configuration.getMapper(type, this);
  }

  public Connection getConnection() {
    return executor.getTransaction().getConnection();
  }

  private boolean isCommitOrRollbackRequired(boolean force) {
    return (!autoCommit && dirty) || force;
  }

  private Object wrapCollection(final Object object) {
    if (object instanceof List) {
      return new HashMap() {{
        put("list", object);
      }};
    } else if (object != null && object.getClass().isArray()) {
      return new HashMap() {{
        put("array", object);
      }};
    }
    return object;
  }

}