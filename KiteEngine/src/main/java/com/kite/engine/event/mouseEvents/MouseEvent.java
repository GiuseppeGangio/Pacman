package com.kite.engine.event.mouseEvents;

import com.kite.engine.event.Event;

public abstract class MouseEvent extends Event
{
    @Override
    public final int GetCategory()
    {
        return EVENT_CATEGORY_MOUSE;
    }
}
