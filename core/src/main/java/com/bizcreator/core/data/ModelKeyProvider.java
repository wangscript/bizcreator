/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

/**
 * Instances of this class provide unique keys for models.
 *
 * @param <M> the model type
 */
public interface ModelKeyProvider<M extends ModelData> {

  /**
   * Returns a unique key for the given model. The key must remain constant for
   * a given model.
   *
   * @param model the model
   * @return the unique key
   */
  public String getKey(M model);

}