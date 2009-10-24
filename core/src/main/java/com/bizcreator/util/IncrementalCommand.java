/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 * An <code>IncrementalCommand</code> is a command that is broken into one or
 * more substeps, each substep brings the whole command nearer to completion.
 * The command is complete when <code>execute()</code> returns
 * <code>false</code>.
 *
 * {@example com.google.gwt.examples.IncrementalCommandExample}
 */
public interface IncrementalCommand {
  /**
   * Causes the <code>IncrementalCommand</code> to execute its encapsulated
   * behavior.
   *
   * @return <code>true</code> if the command has more work to do,
   *         <code>false</code> otherwise
   */
  boolean execute();
}
