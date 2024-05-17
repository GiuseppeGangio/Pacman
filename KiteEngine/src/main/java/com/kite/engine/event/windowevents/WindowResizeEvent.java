package com.kite.engine.event.windowevents;

import com.kite.engine.event.EventType;

public class WindowResizeEvent extends WindowEvent
{
    private final int m_Width, m_Height;

    public WindowResizeEvent (int width, int height)
    {
        m_Width = width;
        m_Height = height;
    }

    @Override public EventType GetType() { return EventType.WINDOW_RESIZED; }

    public int GetWidth () { return m_Width; }
    public int GetHeight () { return m_Height; }

    @Override
    public String toString () { return "WindowResizeEvent(width: " + m_Width + ", height: " + m_Height + ")"; }
}
