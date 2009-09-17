package org.apache.ibatis.executor;

import org.apache.ibatis.executor.keygen.*;
import org.apache.ibatis.executor.result.ResultHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.transaction.Transaction;

import java.sql.*;
import java.util.*;

public class BatchExecutor extends BaseExecutor {

  public static final int BATCH_UPDATE_RETURN_VALUE = Integer.MIN_VALUE + 1002;

  private final List<Statement> statementList = new ArrayList<Statement>();
  private final List<BatchResult> batchResultList = new ArrayList<BatchResult>();
  private String currentSql;

  public BatchExecutor(Transaction transaction) {
    super(transaction);
  }

  public int doUpdate(MappedStatement ms, Object parameterObject)
      throws SQLException {
    Configuration configuration = ms.getConfiguration();
    StatementHandler handler = configuration.newStatementHandler(this, ms, parameterObject, Executor.NO_ROW_OFFSET, Executor.NO_ROW_LIMIT, null);
    BoundSql boundSql = handler.getBoundSql();
    String sql = boundSql.getSql();
    Statement stmt;
    if (currentSql != null && sql.hashCode() == currentSql.hashCode() && sql.length() == currentSql.length()) {
      int last = statementList.size() - 1;
      stmt = statementList.get(last);
    } else {
      Connection connection = transaction.getConnection();
      stmt = handler.prepare(connection);
      currentSql = sql;
      statementList.add(stmt);
      batchResultList.add(new BatchResult(ms, sql, parameterObject));
    }
    handler.parameterize(stmt);
    handler.batch(stmt);
    return BATCH_UPDATE_RETURN_VALUE;
  }

  public List doQuery(MappedStatement ms, Object parameterObject, int rowOffset, int rowLimit, ResultHandler resultHandler)
      throws SQLException {
    flushStatements();
    Configuration configuration = ms.getConfiguration();
    StatementHandler handler = configuration.newStatementHandler(this, ms, parameterObject, rowOffset, rowLimit, resultHandler);
    Connection connection = transaction.getConnection();
    Statement stmt = handler.prepare(connection);
    handler.parameterize(stmt);
    return handler.query(stmt, resultHandler);
  }

  public List<BatchResult> doFlushStatements() throws SQLException {
    List<BatchResult> results = new ArrayList<BatchResult>();
    try {
      for (int i = 0, n = statementList.size(); i < n; i++) {
        Statement stmt = statementList.get(i);
        BatchResult batchResult = batchResultList.get(i);
        try {
          batchResult.setUpdateCounts(stmt.executeBatch());
          MappedStatement ms = batchResult.getMappedStatement();
          Object parameter = batchResult.getParameterObject();
          KeyGenerator keyGenerator = ms.getKeyGenerator();
          if (keyGenerator instanceof Jdbc3KeyGenerator) {
            keyGenerator.processAfter(this, ms, stmt, parameter);
          }
        } catch (BatchUpdateException e) {
          StringBuffer message = new StringBuffer();
          message.append(batchResult.getMappedStatement().getId())
              .append(" (batch index #")
              .append(i + 1)
              .append(")")
              .append(" failed.");
          if (i > 0) {
            message.append(" ")
                .append(i)
                .append(" prior sub executor(s) completed successfully, but will be rolled back.");
          }
          throw new BatchExecutorException(message.toString(), e, results, batchResult);
        }
        results.add(batchResult);
      }
      return results;
    } finally {
      for (Statement stmt : statementList) {
        closeStatement(stmt);
      }
      currentSql = null;
      statementList.clear();
      batchResultList.clear();
    }
  }

}





