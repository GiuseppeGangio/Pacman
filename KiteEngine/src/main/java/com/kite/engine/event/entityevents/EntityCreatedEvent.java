package com.kite.engine.event.entityevents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class EntityCreatedEvent extends EntityEvent
{
    public EntityCreatedEvent (Scene sceneRef, int id)
    {
        super(sceneRef, id);
    }

    @Override
    public EventType GetType ()
    {
        return EventType.ENTITY_CREATED;
    }

    @Override
    public String toString ()
    {
        return "EntityCreatedEvent(" + EntityID + ")";
    }
}
