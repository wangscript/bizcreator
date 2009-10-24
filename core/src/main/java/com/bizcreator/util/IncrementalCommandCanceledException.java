/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 * Exception reported to the current
 * {@link com.google.gwt.core.client.GWT.UncaughtExceptionHandler} when a
 * deferred {@link IncrementalCommand} is canceled as a result of a slow script
 * warning.
 */
public class IncrementalCommandCanceledException extends RuntimeException {
  private IncrementalCommand command;

  public IncrementalCommandCanceledException(IncrementalCommand command) {
    this.command = command;
  }

  /**
   * Returns the {@link IncrementalCommand} which was canceled by the user as a
   * result of a slow script warning.
   *
   * @return the {@link IncrementalCommand} which was canceled by the user as a
   *         result of a slow script warning
   */
  public IncrementalCommand getCommand() {
    return command;
  }
}
