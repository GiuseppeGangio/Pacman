package com.kite.engine.event.entityevents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.Event;

public abstract class EntityEvent extends Event
{
    public Scene SceneRef;
    public int EntityID;

    public EntityEvent (Scene sceneRef, int id)
    {
        SceneRef = sceneRef;
        EntityID = id;
    }

    @Override
    public int GetCategory ()
    {
        return EVENT_CATEGORY_ENTITY;
    }

    @Override
    public String toString ()
    {
        return "EntityEvent";
    }
}
