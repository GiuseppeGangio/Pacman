package com.kite.engine.event.mouseEvents;

import com.kite.engine.event.EventType;

import static com.kite.engine.event.EventType.MOUSE_BUTTON_RELEASED;

public class MouseButtonReleasedEvent extends MouseButtonEvent
{
    public MouseButtonReleasedEvent(int mouseButtonCode)
    {
        super(mouseButtonCode);
    }

    @Override
    public EventType GetType() { return MOUSE_BUTTON_RELEASED; }

    @Override
    public String toString() { return "MouseButtonReleasedEvent(" + p_MouseButtonCode + ")"; }
}
