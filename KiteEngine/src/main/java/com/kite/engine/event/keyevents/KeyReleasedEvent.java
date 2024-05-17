package com.kite.engine.event.keyevents;

import com.kite.engine.event.EventType;

public class KeyReleasedEvent extends KeyEvent
{
    public KeyReleasedEvent(int keyCode)
    {
        super(keyCode);
    }

    @Override
    public EventType GetType() { return EventType.KEY_RELEASED; }

    @Override
    public String toString () { return "KeyReleasedEvent(keyCode: " + p_KeyCode + ")"; }
}
