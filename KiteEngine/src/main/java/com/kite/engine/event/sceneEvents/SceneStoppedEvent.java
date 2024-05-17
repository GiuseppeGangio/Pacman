package com.kite.engine.event.sceneEvents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class SceneStoppedEvent extends SceneEvent
{
    public SceneStoppedEvent (Scene ref)
    {
        super(ref);
    }

    @Override
    public EventType GetType ()
    {
        return EventType.SCENE_STOPPED;
    }

    @Override
    public String toString ()
    {
        return "SceneStoppedEvent";
    }
}

