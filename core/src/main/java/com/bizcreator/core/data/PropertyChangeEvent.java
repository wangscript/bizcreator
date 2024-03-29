/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

/**
 * A <code>ChangeEvent</code> for property changes.
 */
public class PropertyChangeEvent extends ChangeEvent {

  private String name;
  private Object oldValue;
  private Object newValue;

  public PropertyChangeEvent(int type, Model source, String name, Object oldValue, Object newValue) {
    super(type, source);
    this.name = name;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /**
   * Returns the new value.
   *
   * @return the new value
   */
  public Object getNewValue() {
    return newValue;
  }

  /**
   * Returns the old value.
   *
   * @return the old value
   */
  public Object getOldValue() {
    return oldValue;
  }

  /**
   * Returns the property name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

}
