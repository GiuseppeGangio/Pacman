package com.kite.engine.event.entityevents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class EntityComponentAddedEvent extends EntityEvent
{
    public Class<?> ComponentType;
    public Object Component;

    public EntityComponentAddedEvent (Scene sceneRef, int entityID, Class<?> componentType, Object component)
    {
        super(sceneRef, entityID);
        ComponentType = componentType;
        Component = component;
    }

    @Override
    public EventType GetType ()
    {
        return EventType.ENTITY_COMPONENT_ADDED;
    }

    @Override
    public String toString ()
    {
        return "EntityComponentAddedEvent(" + ComponentType.getTypeName() + ")";
    }
}
