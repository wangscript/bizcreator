/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.config;

public class ConfigException extends RuntimeException {

	  public ConfigException() {
	    super();
	  }
	  
	  public ConfigException(String message, Throwable cause) {
	    super(message, cause);
	  }
	  
	  public ConfigException(String message) {
	    super(message);
	  }
	  
	  public ConfigException(Throwable cause) {
	    super(cause);
	  }
}