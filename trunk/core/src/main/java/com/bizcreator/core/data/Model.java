/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.data;

import java.util.List;

/**
 * Primary interface for GXT model objects with event support.
 *
 * <dl>
 * <dt><b>Events:</b></dt>
 *
 * <dd><b>ChangeEventSource.Update</b> : ChangeEvent(source, name, oldvalue, newvalue)<br>
 * <div>Fires after the item is updated.</div>
 * <ul>
 * <li>source : this</li>
 * <li>name : property name </li>
 * <li>oldvalue : old value</li>
 * <li>newvalue : new value</li>
 * </ul>
 * </dd>
 * </dl>
 *
 * <p/>For a default implementation see {@link BaseModel} or
 * {@link BaseTreeModel}.
 */
public interface Model extends ModelData, ChangeEventSource {

    public void addChangeListener(List<ChangeListener> listeners);

}
