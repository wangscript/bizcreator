/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.util;

import java.util.ArrayList;

/**
 * 处理繁忙监听
 * @author lgh
 */
public class BusyHelper {

    /** list of listener of busy state events */
    private static ArrayList busyListeners = new ArrayList();

    /**
     * Add a listener of busy state events.
     * @param busyListener listener of busy state events
     */
    public static final void addBusyListener(BusyListener busyListener) {
        busyListeners.add(busyListener);
    }

    /**
     * Remove a listener of busy state events.
     * @param busyListener listener of busy state events
     */
    public static final void removeBusyListener(BusyListener busyListener) {
        busyListeners.remove(busyListener);
    }

    public static final void fireBusyEvent(boolean busy) {
        try {
            for (int i = 0; i < busyListeners.size(); i++) {
                ((BusyListener) busyListeners.get(i)).setBusy(busy);
            }
        } catch (Exception ex1) {
        }
    }
}
