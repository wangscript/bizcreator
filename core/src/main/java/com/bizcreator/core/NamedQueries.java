/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

/**
 * 管理命名的查询串, 其具体实现在web.xml中设置"NAMED_QUERIES"参数
 * @author Administrator
 */
public interface NamedQueries {
    public String getQlString(String name);
}
