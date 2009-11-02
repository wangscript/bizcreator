package com.bizcreator.core.ibatis;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;

import java.io.Reader;
import org.apache.ibatis.io.Resources;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.Configuration;

// Override SqlSessionFactoryBuilder only to create the necessary
// BeanSqlSessionFactory
public final class SpringSqlSessionFactoryBuilder implements FactoryBean, InitializingBean {

    private SqlSessionFactory sqlSessionFactory;
    private String configLocation;
    private Properties properties;
    private DataSource dataSource;

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlSessionFactory getObject() throws Exception {
        return sqlSessionFactory;
    }

    public Class<? extends SqlSessionFactory> getObjectType() {
        return sqlSessionFactory == null ? SqlSessionFactory.class : sqlSessionFactory.getClass();
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws java.io.IOException {
        if (configLocation == null) {
            throw new IllegalArgumentException("Property 'configLocation' is required");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }

        Reader reader = Resources.getResourceAsReader(configLocation);

        XMLConfigBuilder parser = new XMLConfigBuilder(reader, null, properties);
        Configuration config = parser.parse();

        // override any environment from the configuration file
        // this will cause DefaultSqlSessionFactory to use a
        // ManagedTransactionFactory and a null DataSource
        // note this also means any SqlSessions opened from the factory must be
        // passed a Connection
        config.setEnvironment(null);

        sqlSessionFactory = new DefaultSqlSessionFactory(config);
    }
}
