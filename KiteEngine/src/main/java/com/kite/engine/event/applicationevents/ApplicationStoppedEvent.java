package com.kite.engine.event.applicationevents;

import com.kite.engine.event.EventType;

public class ApplicationStoppedEvent extends ApplicationEvent
{

    @Override
    public EventType GetType ()
    {
        return EventType.APPLICATION_STOPPED;
    }

    @Override
    public String toString ()
    {
        return "ApplicationStoppedEvent";
    }

}
