package com.kite.engine.event.sceneEvents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.Event;

public abstract class SceneEvent extends Event
{
    public Scene SceneRef;

    public SceneEvent (Scene ref)
    {
        SceneRef = ref;
    }

    @Override
    public int GetCategory ()
    {
        return EVENT_CATEGORY_SCENE;
    }

    @Override
    public String toString ()
    {
        return "SceneEvent";
    }
}
