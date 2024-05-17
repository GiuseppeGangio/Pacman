package com.kite.engine.ecs.components;

import com.kite.engine.ecs.Entity;
import com.kite.engine.event.Event;

public abstract class ScriptComponent
{
    protected Entity entity;

    protected ScriptComponent () {}

    public void OnAttach() {}
    public void OnUpdate() {}
    public void OnEvent(Event event) {}
    public void OnCollision (Entity entity) {}
    public void OnGizmo () {}

    public void SetEntity (Entity entity) { this.entity = entity; }
}
