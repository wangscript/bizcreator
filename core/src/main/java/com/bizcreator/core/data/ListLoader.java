/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import com.bizcreator.core.data.SortInfo.SortDir;

/**
 * Interface for list based loaders.
 *
 * @param <D> the data type being returned by the loader
 */
public interface ListLoader<D> extends Loader<D> {

  /**
   * Returns <code>true</code> if remote sorting is enabled.
   *
   * @return the remote sort state
   */
  public boolean isRemoteSort();

  /**
   * Returns the current sort direction.
   *
   * @return the sort direction
   */
  public SortDir getSortDir();

  /**
   * Returns the current sort field.
   *
   * @return the sort field
   */
  public String getSortField();

  /**
   * Sets the current sort direction.
   *
   * @param dir the sort direction
   */
  public void setSortDir(SortDir dir);

  /**
   * Sets the current sort field.
   *
   * @param field the sort field
   */
  public void setSortField(String field);

  /**
   * Sets the remote sort state.
   *
   * @param remote true for remote sort, false for local sorting
   */
  public void setRemoteSort(boolean remote);

}
