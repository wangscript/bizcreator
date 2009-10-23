/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import java.util.List;

/**
 * Load result interface for list based load results.
 *
 * @param <Data> the result data type
 */
public interface ListLoadResult<Data> {

  /**
   * Returns the remote data.
   *
   * @return the data
   */
  public List<Data> getData();

}
