/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 * Exception reported to the current
 * {@link com.google.gwt.core.client.GWT.UncaughtExceptionHandler} when a
 * deferred {@link Command} is canceled as a result of a slow script warning.
 */
public class CommandCanceledException extends RuntimeException {
  private Command command;

  public CommandCanceledException(Command command) {
    this.command = command;
  }

  /**
   * Returns the {@link Command} which was canceled by the user as a result of a
   * slow script warning.
   *
   * @return the {@link Command} which was canceled by the user as a result of a
   *         slow script warning
   */
  public Command getCommand() {
    return command;
  }
}