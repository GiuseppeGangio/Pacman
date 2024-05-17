package com.kite.engine.event.applicationevents;

import com.kite.engine.event.EventType;

public class ApplicationStartedEvent extends ApplicationEvent
{

    @Override
    public EventType GetType ()
    {
        return EventType.APPLICATION_STARTED;
    }

    @Override
    public String toString ()
    {
        return "ApplicationStartEvent";
    }
}
