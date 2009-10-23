/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the <code>ChangeEventSource</code> interface.
 */
public class ChangeEventSupport implements ChangeEventSource {

    protected List<ChangeListener> listeners;
    protected boolean silent;

    public void addChangeListener(ChangeListener... listener) {
        if (listeners == null) {
            listeners = new ArrayList<ChangeListener>();
        }
        for (int i = 0; i < listener.length; i++) {
            if (!hasListener(listener[i])) {
                listeners.add(listener[i]);
            }
        }
    }

    public boolean hasListener(ChangeListener listener) {
        if (listeners == null) return false;
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i) == listener) return true;
        }
        return false;
    }

    public void notify(ChangeEvent event) {
        if (!silent && listeners != null) {
            for (ChangeListener listener : listeners) {
                listener.modelChanged(event);
            }
        }
    }

    public void removeChangeListener(ChangeListener... listener) {
        if (listeners != null) {
            for (int i = 0; i < listener.length; i++) {
                listeners.remove(listener[i]);
            }
        }
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    public void removeChangeListeners() {
        if (listeners != null) {
            listeners.clear();
        }
    }

    
}
