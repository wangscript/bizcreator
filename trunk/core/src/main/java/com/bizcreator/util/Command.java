/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 * Encapsulates an action for later execution, often from a different context.
 *
 * <p>
 * The Command interface provides a layer of separation between the code
 * specifying some behavior and the code invoking that behavior. This separation
 * aids in creating reusable code. For example, a
 * {@link com.google.gwt.user.client.ui.MenuItem} can have a Command
 * associated with it that it executes when the menu item is chosen by the user.
 * Importantly, the code that constructed the Command to be executed when the
 * menu item is invoked knows nothing about the internals of the MenuItem class
 * and vice-versa.</p>
 *
 * <p> The Command interface is often implemented with an anonymous inner class.
 * For example,
 *
 * <pre>
 * Command sayHello = new Command() {
 *   public void execute() {
 *     Window.alert("Hello");
 *   }
 * };
 * sayHello.execute();
 * </pre>
 *
 * </p>
 */
public interface Command {

  /**
   * Causes the Command to perform its encapsulated behavior.
   */
  void execute();
}
