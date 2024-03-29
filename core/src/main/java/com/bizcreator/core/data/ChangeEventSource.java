package com.bizcreator.core.data;

/**
 * Interface for object that notify listeners when changed.
 */
public interface ChangeEventSource {

  /**
   * Fired when a child object is added to the model (value is 10).
   */
  public static final int Add = 10;

  /**
   * Fired when a child object is removed from the model (value is 30).
   */
  public static final int Remove = 30;

  /**
   * Fired when the model has been updated (value is 40).
   */
  public static final int Update = 40;

  /**
   * Adds a change listener to the model.lgh
   *
   * @param listener the listener to add
   */
  public void addChangeListener(ChangeListener... listener);

  /**
   * Removes a change listener.
   *
   * @param listener the listener to remove
   */
  public void removeChangeListener(ChangeListener... listener);

  /**
   * Removes all change listeners.
   */
  public void removeChangeListeners();

  /**
   * Sets whether change events are fired.
   *
   * @param silent true to disable change event, otherwise false
   */
  public void setSilent(boolean silent);

  /**
   * Notifies listeners of the given change event.
   *
   * @param event the change event
   */
  public void notify(ChangeEvent event);
}
