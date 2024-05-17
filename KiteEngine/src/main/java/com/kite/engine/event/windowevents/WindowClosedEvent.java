package com.kite.engine.event.windowevents;

import com.kite.engine.event.EventType;

public class WindowClosedEvent extends WindowEvent
{

    public WindowClosedEvent () {}

    @Override public EventType GetType() { return EventType.WINDOW_CLOSED; }

    @Override
    public String toString () { return "WindowClosedEvent"; }
}
