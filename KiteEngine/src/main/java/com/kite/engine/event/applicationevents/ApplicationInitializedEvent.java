package com.kite.engine.event.applicationevents;

import com.kite.engine.event.EventType;

public class ApplicationInitializedEvent extends ApplicationEvent
{
    public ApplicationInitializedEvent ()
    {

    }

    @Override
    public EventType GetType () { return EventType.APPLICATION_INITIALIZED; }

    @Override
    public String toString ()
    {
        return "ApplicationInitializedEvent";
    }
}
