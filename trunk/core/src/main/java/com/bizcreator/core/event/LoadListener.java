/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.event;

import com.bizcreator.core.data.LoadEvent;
import com.bizcreator.core.data.Loader;

/**
 * Listener for <code>Loader</code> events.
 */
public class LoadListener implements Listener<LoadEvent> {

  public void handleEvent(LoadEvent e) {
    EventType type = e.getType();
    if (type == Loader.BeforeLoad) {
      loaderBeforeLoad(e);
    } else if (type == Loader.Load) {
      loaderLoad(e);
    } else if (type == Loader.LoadException) {
      loaderLoadException(e);
    }
  }

  /**
   * Fires before a load operation begins. Action can be cancelled by calling
   * {@link BaseEvent#setCancelled(boolean)}.
   *
   * @param le the load event
   */
  public void loaderBeforeLoad(LoadEvent le) {

  }

  /**
   * Fires when an exception occurs during a load operation.
   *
   * @param le the load event
   */
  public void loaderLoadException(LoadEvent le) {

  }

  /**
   * Fires after a load operation completes.
   *
   * @param le the load event
   */
  public void loaderLoad(LoadEvent le) {

  }
}
