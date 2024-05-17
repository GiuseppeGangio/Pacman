package com.kite.engine.event.keyevents;

import com.kite.engine.event.EventType;

public class KeyPressedEvent extends KeyEvent
{
    private boolean m_Repeat;

    public KeyPressedEvent(int keyCode, boolean repeat)
    {
        super(keyCode);
        m_Repeat = repeat;
    }

    public boolean Repeats () { return m_Repeat; }

    @Override
    public EventType GetType() { return EventType.KEY_PRESSED; }

    @Override
    public String toString () { return "KeyPressedEvent(keyCode: " + p_KeyCode + ", repeating: " + m_Repeat + ")"; }
}
