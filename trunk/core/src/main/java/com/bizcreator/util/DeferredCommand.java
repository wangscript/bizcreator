/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 * This class allows you to execute code after all currently pending event
 * handlers have completed, using the {@link #addCommand(Command)} or
 * {@link #addCommand(IncrementalCommand)} methods. This is useful when you need
 * to execute code outside of the context of the current stack.
 */
public class DeferredCommand {
  private static final CommandExecutor commandExecutor = new CommandExecutor();

  /**
   * Enqueues a {@link Command} to be fired after all current events have been
   * handled.
   *
   * @param cmd the command to be fired. If cmd is null, a "pause" will be
   *          inserted into the queue. Any events added after the pause will
   *          wait for an additional cycle through the system event loop before
   *          executing. Pauses are cumulative.
   *
   * @deprecated As of release 1.4, replaced by {@link #addCommand(Command)}
   */
  @Deprecated
  public static void add(Command cmd) {
    commandExecutor.submit(cmd);
  }

  /**
   * Enqueues a {@link Command} to be fired after all current events have been
   * handled.
   *
   * Note that the {@link Command} should not perform any blocking operations.
   *
   * @param cmd the command to be fired
   * @throws NullPointerException if cmd is <code>null</code>
   */
  public static void addCommand(Command cmd) {
    if (cmd == null) {
      throw new NullPointerException("cmd cannot be null");
    }

    commandExecutor.submit(cmd);
  }

  /**
   * Enqueues an {@link IncrementalCommand} to be fired after all current events
   * have been handled.
   *
   * Note that the {@link IncrementalCommand} should not perform any blocking
   * operations.
   *
   * @param cmd the command to be fired
   * @throws NullPointerException if cmd is <code>null</code>
   */
  public static void addCommand(IncrementalCommand cmd) {
    if (cmd == null) {
      throw new NullPointerException("cmd cannot be null");
    }

    commandExecutor.submit(cmd);
  }

  /**
   * Adds a "pause" to the queue of {@link DeferredCommand}s. Any
   * {@link DeferredCommand}s or pauses that are added after this pause will
   * wait for an additional cycle through the system event loop before
   * executing.
   */
  public static void addPause() {
    commandExecutor.submit((Command) null);
  }
}
