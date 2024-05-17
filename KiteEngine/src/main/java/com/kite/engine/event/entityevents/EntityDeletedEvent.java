package com.kite.engine.event.entityevents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class EntityDeletedEvent extends EntityEvent
{
    public EntityDeletedEvent (Scene sceneRef, int id)
    {
        super(sceneRef, id);
    }

    @Override
    public EventType GetType ()
    {
        return EventType.ENTITY_DELETED;
    }
}
