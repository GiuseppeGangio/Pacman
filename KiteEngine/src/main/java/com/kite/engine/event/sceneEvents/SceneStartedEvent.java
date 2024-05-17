package com.kite.engine.event.sceneEvents;


import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class SceneStartedEvent extends SceneEvent
{
    public SceneStartedEvent (Scene ref)
    {
        super(ref);
    }

    @Override
    public EventType GetType ()
    {
        return EventType.SCENE_STARTED;
    }

    @Override
    public String toString ()
    {
        return "SceneStartedEvent";
    }
}
