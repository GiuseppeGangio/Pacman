package com.kite.engine.event.windowevents;

import com.kite.engine.event.Event;

public abstract class WindowEvent extends Event
{
    @Override
    public int GetCategory ()
    {
        return EVENT_CATEGORY_WINDOW;
    }

    @Override
    public String toString ()
    {
        return "WindowEvent";
    }
}
