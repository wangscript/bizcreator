package org.apache.ibatis.migration.commands;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.*;
import org.apache.ibatis.migration.*;
import org.apache.ibatis.parsing.PropertyParser;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class BaseCommand implements Command {

  protected static final PrintStream out = System.out;

  protected File basePath;
  protected File envPath;
  protected File scriptPath;
  protected File driverPath;
  protected String environment;
  protected boolean force;
  private ClassLoader driverClassLoader;

  protected BaseCommand(File repository, String environment, boolean force) {
    this.basePath = repository;
    this.envPath = subdirectory(repository, "environments");
    this.scriptPath = subdirectory(repository, "scripts");
    this.driverPath = subdirectory(repository, "drivers");
    this.environment = environment;
    this.force = force;
  }

  protected boolean paramsEmpty(String... params) {
    return params == null || params.length < 1 || params[0] == null || params[0].length() < 1;
  }

  protected List<Change> getMigrations() {
    String[] filenames = scriptPath.list();
    if (filenames == null) throw new MigrationException(scriptPath + " does not exist.");
    Arrays.sort(filenames);
    List<Change> migrations = new ArrayList<Change>();
    for (String filename : filenames) {
      if (filename.endsWith(".sql") && !"bootstrap.sql".equals(filename)) {
        Change change = parseChangeFromFilename(filename);
        migrations.add(change);
      }
    }
    return migrations;
  }

  protected List<Change> getChangelog() {
    SqlRunner runner = getSqlRunner();
    try {
      List<Map<String, Object>> changelog = runner.selectAll("select ID, APPLIED_AT, DESCRIPTION from " + changelogTable() + " order by id");
      List<Change> changes = new ArrayList<Change>();
      for (Map<String, Object> change : changelog) {
        String id = change.get("ID") == null ? null : change.get("ID").toString();
        String appliedAt = change.get("APPLIED_AT") == null ? null : change.get("APPLIED_AT").toString();
        String description = change.get("DESCRIPTION") == null ? null : change.get("DESCRIPTION").toString();
        changes.add(new Change(new BigDecimal(id), appliedAt, description));
      }
      return changes;
    } catch (SQLException e) {
      throw new MigrationException("Error querying last applied migration.  Cause: " + e, e);
    } finally {
      runner.closeConnection();
    }
  }

  protected String changelogTable() {
    String changelog = environmentProperties().getProperty("changelog");
    if (changelog == null) {
      changelog = "CHANGELOG";
    }
    return changelog;
  }

  protected Change getLastAppliedChange() {
    List<Change> changelog = getChangelog();
    return changelog.get(changelog.size() - 1);
  }

  protected boolean changelogExists() {
    SqlRunner runner = getSqlRunner();
    try {
      runner.selectAll("select ID, APPLIED_AT, DESCRIPTION from " + changelogTable());
      return true;
    } catch (SQLException e) {
      return false;
    } finally {
      runner.closeConnection();
    }
  }

  protected String horizontalLine(String caption, int length) {
    StringBuilder builder = new StringBuilder();
    builder.append("==========");
    if (caption.length() > 0) {
      caption = " " + caption + " ";
      builder.append(caption);
    }
    for (int i = 0; i < length - caption.length() - 10; i++) {
      builder.append("=");
    }
    return builder.toString();
  }

  protected String getNextIDAsString() {
    try {
      // Ensure that two subsequent calls are less likely to return the same value.
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      //ignore
    }
    return new SimpleDateFormat("yyyyMMddHHmmss").format(new java.sql.Date(System.currentTimeMillis()));
  }

  protected void copyResourceTo(String resource, File toFile) {
    copyResourceTo(resource, toFile, null);
  }

  protected void copyResourceTo(String resource, File toFile, Properties variables) {
    out.println("Creating: " + toFile.getName());
    try {
      LineNumberReader reader = new LineNumberReader(Resources.getResourceAsReader(this.getClass().getClassLoader(), resource));
      try {
        PrintWriter writer = new PrintWriter(new FileWriter(toFile));
        try {
          String line;
          while ((line = reader.readLine()) != null) {
            line = PropertyParser.parse(line, variables);
            writer.println(line);
          }
        } finally {
          writer.close();
        }
      } finally {
        reader.close();
      }
    } catch (IOException e) {
      throw new MigrationException("Error copying " + resource + " to " + toFile.getAbsolutePath() + ".  Cause: " + e, e);
    }
  }

  protected SqlRunner getSqlRunner() {
    try {
      lazyInitializeDrivers();

      Properties props = environmentProperties();
      String driver = props.getProperty("driver");
      String url = props.getProperty("url");
      String username = props.getProperty("username");
      String password = props.getProperty("password");

      UnpooledDataSource dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
      dataSource.setAutoCommit(true);
      return new SqlRunner(dataSource.getConnection());
    } catch (SQLException e) {
      throw new MigrationException("Could not create SqlRunner. Cause: " + e, e);
    }
  }

  protected ScriptRunner getScriptRunner() {
    try {
      lazyInitializeDrivers();

      Properties props = environmentProperties();
      String driver = props.getProperty("driver");
      String url = props.getProperty("url");
      String username = props.getProperty("username");
      String password = props.getProperty("password");
      PrintWriter outWriter = new PrintWriter(out);
      UnpooledDataSource dataSource = new UnpooledDataSource(driverClassLoader, driver, url, username, password);
      dataSource.setAutoCommit(false);
      ScriptRunner scriptRunner = new ScriptRunner(dataSource.getConnection());
      scriptRunner.setStopOnError(!force);
      scriptRunner.setLogWriter(outWriter);
      scriptRunner.setErrorLogWriter(outWriter);
      setPropertiesFromFile(scriptRunner, props);
      return scriptRunner;
    } catch (Exception e) {
      throw new MigrationException("Error creating ScriptRunner.  Cause: " + e, e);
    }
  }

  private void setPropertiesFromFile(ScriptRunner scriptRunner, Properties props) {
    String delimiterString = props.getProperty("delimiter");
    scriptRunner.setAutoCommit(Boolean.valueOf(props.getProperty("auto_commit")));
    scriptRunner.setDelimiter(delimiterString == null ? ";" : delimiterString);
    scriptRunner.setFullLineDelimiter(Boolean.valueOf(props.getProperty("full_line_delimiter")));
    scriptRunner.setSendFullScript(Boolean.valueOf(props.getProperty("send_full_script")));
  }

  protected File baseFile(String fileName) {
    return new File(basePath.getAbsolutePath() + File.separator + fileName);
  }

  protected File environmentFile(String fileName) {
    return new File(envPath.getAbsolutePath() + File.separator + fileName);
  }

  protected File scriptFile(String fileName) {
    return new File(scriptPath.getAbsolutePath() + File.separator + fileName);
  }

  protected File driverFile(String fileName) {
    return new File(driverPath.getAbsolutePath() + File.separator + fileName);
  }

  protected File environmentFile() {
    return environmentFile(environment + ".properties");
  }

  protected File existingEnvironmentFile() {
    File envFile = environmentFile();
    if (!envFile.exists()) {
      throw new MigrationException("Environment file missing: " + envFile.getAbsolutePath());
    }
    return envFile;
  }

  private void lazyInitializeDrivers() {
    try {
      if (driverClassLoader == null && driverPath.exists()) {
        List<URL> urlList = new ArrayList<URL>();
        for (File file : driverPath.listFiles()) {
          String filename = file.getCanonicalPath();
          if (!filename.startsWith("/")) {
            filename = "/" + filename;
          }
          urlList.add(new URL("jar:file:" + filename + "!/"));
          urlList.add(new URL("file:" + filename));
        }
        URL[] urls = urlList.toArray(new URL[urlList.size()]);
        driverClassLoader = new URLClassLoader(urls);
      }
    } catch (IOException e) {
      throw new MigrationException("Error loading JDBC drivers. Cause: " + e, e);
    }
  }

  protected Properties environmentProperties() {
    try {
      File file = existingEnvironmentFile();
      Properties props = new Properties();
      props.load(new FileInputStream(file));
      return props;
    } catch (IOException e) {
      throw new MigrationException("Error loading environment properties.  Cause: " + e, e);
    }
  }

  protected void insertChangelog(Change change) {
    SqlRunner runner = getSqlRunner();
    change.setAppliedTimestamp(generateAppliedTimeStampAsString());
    try {
      runner.insert("insert into " + changelogTable() + " (ID, APPLIED_AT, DESCRIPTION) values (?,?,?)", change.getId(), change.getAppliedTimestamp(), change.getDescription());
    } catch (SQLException e) {
      throw new MigrationException("Error querying last applied migration.  Cause: " + e, e);
    } finally {
      runner.closeConnection();
    }
  }

  protected String generateAppliedTimeStampAsString() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.sql.Date(System.currentTimeMillis()));
  }

  private File subdirectory(File base, String sub) {
    return new File(base.getAbsoluteFile() + File.separator + sub);
  }

  private Change parseChangeFromFilename(String filename) {
    try {
      Change change = new Change();
      String[] parts = filename.split("\\.")[0].split("_");
      change.setId(new BigDecimal(parts[0]));
      StringBuilder builder = new StringBuilder();
      for (int i = 1; i < parts.length; i++) {
        if (i > 1) builder.append(" ");
        builder.append(parts[i]);
      }
      change.setDescription(builder.toString());
      change.setFilename(filename);
      return change;
    } catch (Exception e) {
      throw new MigrationException("Error parsing change from file.  Cause: " + e, e);
    }
  }

}
