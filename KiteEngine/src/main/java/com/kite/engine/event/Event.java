package com.kite.engine.event;

import java.util.function.Function;

public abstract class Event
{
    public static final int EVENT_CATEGORY_KEY    = 0x1;
    public static final int EVENT_CATEGORY_MOUSE  = 0x1 << 1;
    public static final int EVENT_CATEGORY_WINDOW = 0x1 << 2;
    public static final int EVENT_CATEGORY_APPLICATION = 0x1 << 3;
    public static final int EVENT_CATEGORY_SCENE = 0x1 << 4;
    public static final int EVENT_CATEGORY_ENTITY = 0x1 << 5;

    public boolean Handled = false;

    public static <T extends Event> void  Dispatch  (Class<T> tClass, Function<T, Boolean> func, Event event)
    {
        if (tClass.isInstance(event))
        {
            event.Handled = func.apply(tClass.cast(event));
        }
    }

    public static int EventTypeToCategory (EventType type)
    {
        switch (type)
        {
            case KEY_PRESSED:
            case KEY_RELEASED:
                return EVENT_CATEGORY_KEY;

            case MOUSE_BUTTON_PRESSED:
            case MOUSE_BUTTON_RELEASED:
            case MOUSE_SCROLLED:
            case MOUSE_MOVED:
                return EVENT_CATEGORY_MOUSE;

            case WINDOW_RESIZED:
            case WINDOW_CLOSED:
                return EVENT_CATEGORY_WINDOW;

            case APPLICATION_INITIALIZED:
            case APPLICATION_STARTED:
            case APPLICATION_STOPPED:
                return EVENT_CATEGORY_APPLICATION;

            case SCENE_STARTED:
            case SCENE_STOPPED:
            case SCENE_ENDED:
                return EVENT_CATEGORY_SCENE;

            case ENTITY_CREATED:
            case ENTITY_COMPONENT_ADDED:
            case ENTITY_COMPONENT_REMOVED:
            case ENTITY_DELETED:
                return EVENT_CATEGORY_ENTITY;
        }

        throw new Error("Type of unspecified category");
    }

    public final boolean IsInCategory (int category) { return (GetCategory() & category) != 0; }

    public abstract EventType GetType ();
    public abstract int GetCategory ();

    @Override
    public String toString () { return "Event"; }
}
