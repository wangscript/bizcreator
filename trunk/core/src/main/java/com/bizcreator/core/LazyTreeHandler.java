package com.bizcreator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 查找满足条件的子节点数据，以便在RhTreeTable中引用
 * @author Administrator
 *
 */
public interface LazyTreeHandler {

    public final static List EMPTY_LIST = new ArrayList();

    public String[] getParentFields();

    public List findTopEntities();

	public List findTopEntities(Object[] params);

	public List findChildren(Object[] params);
}
