package com.kite.engine.event.keyevents;

import com.kite.engine.event.Event;

public abstract class KeyEvent extends Event
{
    protected int p_KeyCode;

    KeyEvent (int keyCode)
    {
        p_KeyCode = keyCode;
    }

    public int KeyCode () { return p_KeyCode; }

    @Override
    public final int GetCategory()
    {
        return EVENT_CATEGORY_KEY;
    }
}
