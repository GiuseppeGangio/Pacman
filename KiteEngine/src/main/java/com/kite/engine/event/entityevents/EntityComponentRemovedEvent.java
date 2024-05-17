package com.kite.engine.event.entityevents;

import com.kite.engine.core.Scene;
import com.kite.engine.event.EventType;

public class EntityComponentRemovedEvent extends EntityEvent
{
    public Class<?> ComponentType;
    public Object Component;

    public EntityComponentRemovedEvent (Scene sceneRef, int entityID, Class<?> componentType, Object component)
    {
        super(sceneRef, entityID);
        ComponentType = componentType;
        Component = component;
    }

    @Override
    public EventType GetType ()
    {
        return EventType.ENTITY_COMPONENT_REMOVED;
    }

    @Override
    public String toString ()
    {
        return "EntityComponentRemovedEvent(" + ComponentType.getTypeName() + ")";
    }
}
