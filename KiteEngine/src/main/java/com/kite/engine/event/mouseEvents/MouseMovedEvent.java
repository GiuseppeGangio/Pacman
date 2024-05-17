package com.kite.engine.event.mouseEvents;

import com.kite.engine.event.EventType;

import static com.kite.engine.event.EventType.MOUSE_MOVED;

public class MouseMovedEvent extends MouseEvent
{
    private float m_MouseX, m_MouseY;

    public MouseMovedEvent (float x, float y)
    {
        m_MouseX = x;
        m_MouseY = y;
    }

    public float GetX () { return m_MouseX; }
    public float GetY () { return m_MouseY; }

    @Override
    public EventType GetType() { return MOUSE_MOVED; }

    @Override
    public String toString() { return "MouseMovedEvent(" + m_MouseX + ", " + m_MouseY + ")"; }
}
