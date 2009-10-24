package com.bizcreator.core;

/**
 * 查询包装接口，用于在任务窗格中构建过滤面板
 * @author Administrator
 *
 */
public abstract class AbstractFilter {

	protected String[] filterFields;
	protected String[] filterNames;
	protected Object[] filterEditors;
	protected String filterSql;

	//过滤字段
	public abstract String[] getFilterFields();

	//过滤字段名称
	public abstract String[] getFilterNames();

	//过滤字段编辑器
	public abstract Object[] getFilterEditors();

	//初始化设置过滤字段编辑器
	public abstract void initFilterEditors();

	//过滤语句
	public abstract String getFilterSql();

	//排序
	public abstract String getOrderBy();

	//初始参数
	public Object[] getInitParams() {
		return null;
	}

}
