package com.kite.engine.event.mouseEvents;

import com.kite.engine.event.EventType;

import static com.kite.engine.event.EventType.MOUSE_SCROLLED;

public class MouseScrolledEvent extends MouseEvent
{
    private float m_ScrollValue;

    public MouseScrolledEvent (float scrollValue)
    {
        m_ScrollValue = scrollValue;
    }

    public float GetScrollValue() {return m_ScrollValue; }

    @Override
    public EventType GetType() { return MOUSE_SCROLLED; }

    @Override
    public String toString() { return "MouseScrolledEvent(" + m_ScrollValue + ")"; }
}
