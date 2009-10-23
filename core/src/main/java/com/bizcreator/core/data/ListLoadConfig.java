/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import com.bizcreator.core.data.SortInfo.SortDir;

/**
 * Load config interface for list based data.
 */
public interface ListLoadConfig extends LoadConfig {

  public SortDir getSortDir();

  public String getSortField();

  /**
   * Returns the sort info.
   */
  public SortInfo getSortInfo();

  public void setSortDir(SortDir sortDir);

  public void setSortField(String sortField);

  /**
   * Sets the sort info.
   */
  public void setSortInfo(SortInfo info);
}
