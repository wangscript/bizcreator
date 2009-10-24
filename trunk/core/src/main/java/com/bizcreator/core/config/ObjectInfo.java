/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.config;

import java.io.Serializable;
import org.picocontainer.MutablePicoContainer;

public interface ObjectInfo extends Serializable {

  boolean hasName();
  String getName();
  boolean isSingleton();

  Object createObject(ObjectFactory objectFactory);
  
  void addToPico(MutablePicoContainer pico);
  
}