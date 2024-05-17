package com.kite.engine.event.sceneEvents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class SceneEndedEvent extends SceneEvent
{
    public SceneEndedEvent (Scene ref)
    {
        super(ref);
    }

    @Override
    public EventType GetType ()
    {
        return EventType.SCENE_ENDED;
    }

    @Override
    public String toString ()
    {
        return "SceneEndedEvent";
    }
}
