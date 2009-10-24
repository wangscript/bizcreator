/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.util;

/**
 *   
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @version $Revision: 1.1 $
 */
public interface Monitorable
{
	// Constants ----------------------------------------------------
	
	// Static -------------------------------------------------------

	// Public -------------------------------------------------------
	/**
	 * Samples the status of the implementor object and register the status
	 * into the snapshot argument.
	 */
	public void sample(Object snapshot);
  
	// Inner classes -------------------------------------------------
}
