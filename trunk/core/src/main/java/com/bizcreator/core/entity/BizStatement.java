/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.entity;

//import org.apache.ibatis.mapping.ResultSetType;
//import org.apache.ibatis.mapping.StatementType;

/**
 *
 * @author lgh
 */
public class BizStatement {

    protected String modelId;
    
    
    protected String id;

    //code -- A unique identifier in this namespace that can be used to reference this statement.
    protected String code;

    protected String name;

    protected String description;

    protected String helpInfo;
    
    // parameterType -- The fully qualified class name or alias for the parameter that will be passed into this
    //                  statement.
    protected String parameterType;

    /* 
     resultType -- The fully qualified class name or alias for the expected type that will be returned
        from this statement. Note that in the case of collections, this should be the type
        that the collection contains, not the type of the collection itself. Use resultType OR
        resultMap, not both.
     */
     protected String resultType;

    /*
     resultMap -- A named reference to an external resultMap. Result maps are the most powerful
        feature of iBATIS, and with a good understanding of them, many difficult mapping
        cases can be solved. Use resultMap OR resultType, not both.
     */
     protected String resultMap;

    /*
     flushCache -- Setting this to true will cause the cache to be flushed whenever this statement is
        called. Default: false for select statements.
     */

    /*
     useCache -- Setting this to true will cause the results of this statement to be cached. Default:
        true for select statements
     */
     protected boolean useCache;

    /*
     timeout -- This sets the maximum time the driver will wait for the database to return from a
        request, before throwing an exception. Default is unset (driver dependent).
     */
     protected int timeout;

    /*
     fetchSize -- This is a driver hint that will attempt to cause the driver to return results in batches
        of rows numbering in size equal to this setting. Default is unset (driver dependent).
     */

     protected int fetchSize;

    /*
     statementType -- Any one of STATEMENT, PREPARED or CALLABLE. This causes iBATIS to use
        Statement, PreparedStatement or CallableStatement respectively. Default:
        PREPARED.
     */
    //protected StatementType stmtType;

    /*
     resultSetType -- Any one of FORWARD_ONLY|SCROLL_SENSITIVE|SCROLL_INSENSITIVE. Default is
        unset (driver dependent).
     */
    //protected ResultSetType rsType;

    //following for insert update delete
    /*
    useGeneratedKeys (insert only) This tells iBATIS to use the JDBC getGeneratedKeys method to retrieve
        keys generated internally by the database (e.g. auto increment fields in RDBMS like
        MySQL or SQL Server). Default: false
     */
    protected boolean useGeneratedKeys;

    /*
     keyProperty (insert only) Identifies a property into which iBATIS will set the key value returned
        by getGeneratedKeys, or by a selectKey child element of the insert statement.
        Default: unset.
     */
    protected String keyProperty;

    /**
     * sql content
     */
    protected String content;


}
