/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import com.bizcreator.core.event.EventType;
import com.bizcreator.core.event.LoadListener;
import com.bizcreator.core.event.Observable;

/**
 *
 * @author lgh
 */
public interface Loader<D> extends Observable {

/**
   * Fires before a request is made for data.
   */
  public static final EventType BeforeLoad = new EventType();

  /**
   * Fires when new data has been loaded.
   */
  public static final EventType Load = new EventType();

  /**
   * Fires if an exception occurs while retrieving data.
   */
  public static final EventType LoadException = new EventType();

  /**
   * Adds a load listener.
   *
   * @param listener the listener to add
   */
  public void addLoadListener(LoadListener listener);

  /**
   *
   * Loads the data using the current configuration.
   *
   * @return true if the load was requested
   */
  public boolean load();

  /**
   * Loads the data using the given load configuration.
   *
   * @param loadConfig the load config
   * @return true if the load was requested
   */
  public boolean load(Object loadConfig);

  /**
   * Removes a load listener.
   *
   * @param listener the listener to remove
   */
  public void removeLoadListener(LoadListener listener);

}

