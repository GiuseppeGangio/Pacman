package com.kite.engine.event.mouseEvents;

import com.kite.engine.event.EventType;

import static com.kite.engine.event.EventType.MOUSE_BUTTON_PRESSED;

public class MouseButtonPressedEvent extends MouseButtonEvent
{
    public MouseButtonPressedEvent(int mouseButtonCode)
    {
        super(mouseButtonCode);
    }

    @Override
    public EventType GetType() { return MOUSE_BUTTON_PRESSED; }

    @Override
    public String toString() { return "MouseButtonPressedEvent(" + p_MouseButtonCode + ")"; }
}
