package com.kite.engine.event.applicationevents;

import com.kite.engine.event.Event;

public abstract class ApplicationEvent extends Event
{
    @Override
    public int GetCategory () { return EVENT_CATEGORY_APPLICATION; }

    @Override
    public String toString ()
    {
        return "ApplicationEvent";
    }
}
