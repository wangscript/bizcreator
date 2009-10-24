package com.bizcreator.core.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.TransactionHelper;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerationException;
import org.hibernate.id.IdentifierGenerator;

import org.hibernate.type.Type;

import com.bizcreator.util.StringUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 在应用域内生成主键标识：
 * 1. 标识的格式：id@域，如：100011166601@rhino
 * 2. id的生成方法：通过表名从MD_Sequence中获得当前的序号；
 * 3. 从系统属性中得到运行的域；
 * 4. 用法如下：
 * 	<id name="id" type="String" column="cat_id">
 *		<generator class="com.rhinofield.id.DomainIdentifierGenerator">
 *			<param name="seqName">CBF_Party</param>
 *		</generator>
 *	</id>
 * 
 * @author <a href="mailto:rhino03142000@yahoo.com">luoguanhua</a>
 * @version $Revision: 1.4 $
 *
 */
public class DomainIdentifierGenerator extends TransactionHelper
        implements IdentifierGenerator, Configurable {

    static final Log SQL = LogFactory.getLog("org.hibernate.SQL");
    /**
     * 所属的应用域，默认为rhino.
     */
    public static final String DOMAIN = "domain";
    public static final String DEFAULT_DOMAIN_NAME = "rhino";
    public static final String SEQ = "seq";
    public static final String TABLE_NAME = "md_sequence";
    /**
     * ID串的长度
     */
    public static final int ID_LENGTH = 12;
    private static final Logger log = Logger.getLogger(DomainIdentifierGenerator.class.getName());
    private static String domainName;
    private String seqName;
    private String query;
    private String update;

    public void configure(Type type, Properties params, Dialect dialect) throws MappingException {

        if (domainName == null) {
            domainName = System.getProperty(DOMAIN, DEFAULT_DOMAIN_NAME);
        }
        seqName = params.getProperty(SEQ);

        query = "select next_value " + " from " + dialect.appendLockHint(LockMode.UPGRADE, TABLE_NAME) + " where domain = '" + domainName + "'" + " and name = '" + seqName + "' " + dialect.getForUpdateString();

        update = "update " + TABLE_NAME + " set next_value = ? " + " where domain = '" + domainName + "'" + " and name = '" + seqName + "'";
    }

    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        int result = ((Integer) doWorkInNewTransaction(session)).intValue();
        String s = StringUtil.padPrefix(result + "", "0", ID_LENGTH) + "@" + domainName;
        return s;
    }

    public Serializable doWorkInCurrentTransaction(Connection conn, String sql) throws SQLException {
        int result;
        int rows;
        do {
            // The loop ensures atomicity of the
            // select + update even for no transaction
            // or read committed isolation level

            sql = query;
            SQL.debug(query);
            PreparedStatement qps = conn.prepareStatement(query);
            try {
                ResultSet rs = qps.executeQuery();
                if (!rs.next()) {
                    String err = "could not read a hi value - you need to populate the table: " + TABLE_NAME;
                    log.log(Level.SEVERE, err);
                    throw new IdentifierGenerationException(err);
                }
                result = rs.getInt(1);
                rs.close();
            } catch (SQLException sqle) {
                log.log(Level.SEVERE, "could not read a hi value", sqle);
                throw sqle;
            } finally {
                qps.close();
            }

            sql = update;
            SQL.debug(update);
            PreparedStatement ups = conn.prepareStatement(update);
            try {
                ups.setInt(1, result + 1);
                rows = ups.executeUpdate();
            } catch (SQLException sqle) {
                log.log(Level.SEVERE, "could not update next_value in: " + TABLE_NAME, sqle);
                throw sqle;
            } finally {
                ups.close();
            }
        } while (rows == 0);
        return new Integer(result);
    }
}
