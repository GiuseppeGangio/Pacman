package com.kite.engine.core;

import com.kite.engine.event.Event;

public abstract class Layer
{
    protected boolean m_Active = true;

    protected void OnAttach () {}
    protected void OnUpdate () {}
    protected void OnEvent (Event event) {}

    protected void Active (boolean active) { m_Active = active; }
    protected boolean Active () { return m_Active; }
}
