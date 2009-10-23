/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

/**
 * Compares the model instances for equality.
 *
 * @param <M> the model type
 */
public interface ModelComparer<M extends ModelData> {

  public boolean equals(M m1, M m2);

}
