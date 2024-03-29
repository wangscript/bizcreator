package com.bizcreator.core.event;

/**
 * Base class for all events types.
 */
public class EventType {

    private int eventCode = -1;
    /**
     * Fired when an element loses keyboard focus.
     */
    public static final int ONBLUR = 0x01000;
    /**
     * Fired when the value of an input element changes.
     */
    public static final int ONCHANGE = 0x00400;
    /**
     * Fired when the user clicks on an element.
     */
    public static final int ONCLICK = 0x00001;
    /**
     * Fired when the user double-clicks on an element.
     */
    public static final int ONDBLCLICK = 0x00002;
    /**
     * Fired when an image encounters an error.
     */
    public static final int ONERROR = 0x10000;
    /**
     * Fired when an element receives keyboard focus.
     */
    public static final int ONFOCUS = 0x00800;
    /**
     * Fired when the user depresses a key.
     */
    public static final int ONKEYDOWN = 0x00080;
    /**
     * Fired when the a character is generated from a keypress (either directly or
     * through auto-repeat).
     */
    public static final int ONKEYPRESS = 0x00100;
    /**
     * Fired when the user releases a key.
     */
    public static final int ONKEYUP = 0x00200;
    /**
     * Fired when an element (normally an IMG) finishes loading.
     */
    public static final int ONLOAD = 0x08000;
    /**
     * Fired when an element that has mouse capture loses it.
     */
    public static final int ONLOSECAPTURE = 0x02000;
    /**
     * Fired when the user depresses a mouse button over an element.
     */
    public static final int ONMOUSEDOWN = 0x00004;
    /**
     * Fired when the mouse is moved within an element's area.
     */
    public static final int ONMOUSEMOVE = 0x00040;
    /**
     * Fired when the mouse is moved out of an element's area.
     */
    public static final int ONMOUSEOUT = 0x00020;
    /**
     * Fired when the mouse is moved into an element's area.
     */
    public static final int ONMOUSEOVER = 0x00010;
    /**
     * Fired when the user releases a mouse button over an element.
     */
    public static final int ONMOUSEUP = 0x00008;
    /**
     * Fired when the user scrolls the mouse wheel over an element.
     */
    public static final int ONMOUSEWHEEL = 0x20000;
    /**
     * Fired when a scrollable element's scroll offset changes.
     */
    public static final int ONSCROLL = 0x04000;
    /**
     * Fired when the user requests an element's context menu (usually by
     * right-clicking).
     *
     * Note that not all browsers will fire this event (notably Opera, as of 9.5).
     */
    public static final int ONCONTEXTMENU = 0x40000;
    /**
     * A bit-mask covering both focus events (focus and blur).
     */
    public static final int FOCUSEVENTS = ONFOCUS | ONBLUR;
    /**
     * A bit-mask covering all keyboard events (down, up, and press).
     */
    public static final int KEYEVENTS = ONKEYDOWN | ONKEYPRESS | ONKEYUP;
    /**
     * A bit-mask covering all mouse events (down, up, move, over, and out), but
     * not click, dblclick, or wheel events.
     */
    public static final int MOUSEEVENTS = ONMOUSEDOWN | ONMOUSEUP | ONMOUSEMOVE | ONMOUSEOVER | ONMOUSEOUT;

    /**
     * Creates a new event type.
     */
    public EventType() {
    }

    /**
     * Creates a new browser based event type.
     *
     * @param eventCode additional information about the event
     */
    public EventType(int eventCode) {
        this.eventCode = eventCode;
    }

    /**
     * Returns the event code.
     *
     * @return the event code
     * @see Event
     */
    public int getEventCode() {
        return this.eventCode;
    }

    /**
     * Returns true if the event type represents a browser event type (GWT event).
     *
     * @return true for browser event types
     */
    public boolean isBrowserEvent() {
        return eventCode != -1;
    }
}
