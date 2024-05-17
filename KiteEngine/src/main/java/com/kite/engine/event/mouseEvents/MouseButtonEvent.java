package com.kite.engine.event.mouseEvents;

public abstract class MouseButtonEvent extends MouseEvent
{
    protected int p_MouseButtonCode;

    protected MouseButtonEvent (int mouseButtonCode)
    {
        p_MouseButtonCode = mouseButtonCode;
    }

    public int MouseButtonCode () { return p_MouseButtonCode; }
}
