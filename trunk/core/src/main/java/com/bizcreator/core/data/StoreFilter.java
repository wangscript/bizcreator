/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

/**
 * Interface for store filters.
 *
 * @param <T> the model type
 */
public interface StoreFilter<T extends ModelData> {

  /**
   * Determines if the given record should be selected.
   *
   * @param store the source store
   * @param parent the parent item, only applies to TreeStores
   * @param item the item
   * @param property the active property
   * @return true to select, false to filter
   */
  public boolean select(Store<T> store, T parent, T item, String property);

}