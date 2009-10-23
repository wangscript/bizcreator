/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

/**
 * Interface for objects that listen for model change events.
 *
 * @see ChangeEvent
 * @see BaseModel
 */
public interface ChangeListener {

  /**
   * Fired when the model's state has changed.
   *
   * @param event an event describing the change
   */
  public void modelChanged(ChangeEvent event);

}

