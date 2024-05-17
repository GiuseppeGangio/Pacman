package com.kite.engine.event;

import com.kite.engine.event.applicationevents.ApplicationEvent;
import com.kite.engine.event.applicationevents.ApplicationInitializedEvent;
import com.kite.engine.event.applicationevents.ApplicationStartedEvent;
import com.kite.engine.event.applicationevents.ApplicationStoppedEvent;
import com.kite.engine.event.entityevents.EntityEvent;
import com.kite.engine.event.keyevents.KeyEvent;
import com.kite.engine.event.keyevents.KeyPressedEvent;
import com.kite.engine.event.keyevents.KeyReleasedEvent;
import com.kite.engine.event.mouseEvents.*;
import com.kite.engine.event.sceneEvents.*;
import com.kite.engine.event.windowevents.*;
import com.kite.engine.event.entityevents.*;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EventHandler
{
    private static Consumer<Event> s_Callback = event -> {};
    private static final HashMap<EventSubscriptionKey, BiConsumer<Boolean, ? extends Event>> s_Subscribers = new HashMap<>();

    public static void SetCallback (Consumer<Event> callback)
    {
        s_Callback = callback;
    }

    public static void PropagateEvent (Event e)
    {
        s_Callback.accept(e);
    }

    public static void Notify (Event event)
    {
        s_Subscribers.forEach((EventSubscriptionKey key, BiConsumer<Boolean, ? extends Event> callback) ->
        {
            if (event.IsInCategory(key.EventCategory_))
            {
                Class<? extends Event> clazz = null;

                if (key.EventType_ == null )
                    clazz = EventCategoryToClass(key.EventCategory_);
                else if (key.EventType_ == event.GetType())
                    clazz = EventTypeToClass(key.EventType_);

                if (clazz != null)
                    callback.accept(false, CastEvent(clazz, event));
            }
        });
    }


    public static <T extends Event> void Subscribe (EventType eventType, BiConsumer<Boolean, T> callback)
    {
        if (!IsSubscriptionInputValid(eventType, callback))
            throw new Error("Event type incompatible with provided function");

        s_Subscribers.put(new EventSubscriptionKey(eventType), callback);
    }

    public static <T extends Event> void Subscribe (int eventCategory, BiConsumer<Boolean, T> callback)
    {
        if (!IsSubscriptionInputValid(eventCategory, callback))
            throw new Error("Event category incompatible with provided function");

        s_Subscribers.put(new EventSubscriptionKey(eventCategory), callback);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Event> boolean IsSubscriptionInputValid (EventType eventType, BiConsumer<Boolean, T> callback)
    {
        try {
            Event event = GetSimpleEventFromType(eventType);
            callback.accept(true, (T) event);
            return true;
        } catch (ClassCastException exception) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Event> boolean IsSubscriptionInputValid (int eventCategory, BiConsumer<Boolean, T> callback)
    {
        try {
            Event event = GetSimpleEventFromCategory(eventCategory);
            callback.accept(true, (T) event);
            return true;
        } catch (ClassCastException exception) {
            return false;
        }
    }

    private static Class<? extends Event> EventCategoryToClass (int category)
    {
        switch (category)
        {
            case Event.EVENT_CATEGORY_KEY:
                return KeyEvent.class;
            case Event.EVENT_CATEGORY_MOUSE:
                return MouseEvent.class;
            case Event.EVENT_CATEGORY_WINDOW:
                return WindowEvent.class;
            case Event.EVENT_CATEGORY_APPLICATION:
                return ApplicationEvent.class;
            case Event.EVENT_CATEGORY_SCENE:
                return SceneEvent.class;
            case Event.EVENT_CATEGORY_ENTITY:
                return EntityEvent.class;
        }

        throw new Error("Invalid category specified");
    }

    private static Class<? extends Event> EventTypeToClass (EventType type)
    {
        switch (type)
        {
            case KEY_PRESSED:
                return KeyPressedEvent.class;
            case KEY_RELEASED:
                return KeyReleasedEvent.class;
            case MOUSE_BUTTON_PRESSED:
                return MouseButtonPressedEvent.class;
            case MOUSE_BUTTON_RELEASED:
                return MouseButtonReleasedEvent.class;
            case MOUSE_SCROLLED:
                return MouseScrolledEvent.class;
            case MOUSE_MOVED:
                return MouseMovedEvent.class;
            case WINDOW_RESIZED:
                return WindowResizeEvent.class;
            case WINDOW_CLOSED:
                return WindowClosedEvent.class;
            case APPLICATION_INITIALIZED:
                return ApplicationInitializedEvent.class;
            case APPLICATION_STARTED:
                return ApplicationStartedEvent.class;
            case APPLICATION_STOPPED:
                return ApplicationStoppedEvent.class;
            case SCENE_STARTED:
                return SceneStartedEvent.class;
            case SCENE_STOPPED:
                return SceneStoppedEvent.class;
            case SCENE_ENDED:
                return SceneEndedEvent.class;
            case ENTITY_CREATED:
                return EntityCreatedEvent.class;
            case ENTITY_COMPONENT_ADDED:
                return EntityComponentAddedEvent.class;
            case ENTITY_COMPONENT_REMOVED:
                return EntityComponentRemovedEvent.class;
            case ENTITY_DELETED:
                return EntityDeletedEvent.class;
        }

        throw new Error("Invalid type");
    }

    @SuppressWarnings("unchecked")
    private static <T extends Event> T CastEvent (Class<? extends Event> castClass, Event event)
    {
        Class<T> castedClass = (Class<T>) castClass;
        return castedClass.cast(event);
    }

    private static Event GetSimpleEventFromCategory (int category)
    {
        switch (category)
        {
            case Event.EVENT_CATEGORY_KEY:
                return new KeyPressedEvent(0, false);
            case Event.EVENT_CATEGORY_MOUSE:
                return new MouseButtonPressedEvent(0);
            case Event.EVENT_CATEGORY_WINDOW:
                return new WindowResizeEvent(0, 0);
            case Event.EVENT_CATEGORY_APPLICATION:
                return new ApplicationInitializedEvent();
            case Event.EVENT_CATEGORY_SCENE:
                return new SceneStartedEvent(null);
            case Event.EVENT_CATEGORY_ENTITY:
                return new EntityCreatedEvent(null, 0);
        }

        throw new Error("Invalid category specified");
    }

    private static Event GetSimpleEventFromType (EventType type)
    {
        switch (type)
        {
            case KEY_PRESSED:
                return new KeyPressedEvent(0, false);
            case KEY_RELEASED:
                return new KeyReleasedEvent(0);
            case MOUSE_BUTTON_PRESSED:
                return new MouseButtonPressedEvent(0);
            case MOUSE_BUTTON_RELEASED:
                return new MouseButtonReleasedEvent(0);
            case MOUSE_SCROLLED:
                return new MouseScrolledEvent(0);
            case MOUSE_MOVED:
                return new MouseMovedEvent(0, 0);
            case WINDOW_RESIZED:
                return new WindowResizeEvent(0, 0);
            case WINDOW_CLOSED:
                return new WindowClosedEvent();
            case APPLICATION_INITIALIZED:
                return new ApplicationInitializedEvent();
            case APPLICATION_STARTED:
                return new ApplicationStartedEvent();
            case APPLICATION_STOPPED:
                return new ApplicationStoppedEvent();
            case SCENE_STARTED:
                return new SceneStartedEvent(null);
            case SCENE_STOPPED:
                return new SceneStoppedEvent(null);
            case SCENE_ENDED:
                return new SceneEndedEvent(null);
            case ENTITY_CREATED:
                return new EntityCreatedEvent(null, 0);
            case ENTITY_COMPONENT_ADDED:
                return new EntityComponentAddedEvent(null, 0, null, null);
            case ENTITY_COMPONENT_REMOVED:
                return new EntityComponentRemovedEvent(null, 0, null, null);
            case ENTITY_DELETED:
                return new EntityDeletedEvent(null, 0);
        }

        throw new Error("Invalid type");
    }

    private static class EventSubscriptionKey
    {
        int EventCategory_;
        EventType EventType_;

        public EventSubscriptionKey (int eventCategory)
        {
            EventCategory_ = eventCategory;
            EventType_ = null;
        }

        public EventSubscriptionKey (EventType eventType)
        {
            EventType_ = eventType;
            EventCategory_ = Event.EventTypeToCategory(eventType);
        }

        @Override
        public int hashCode ()
        {
            return super.hashCode();
        }

        @Override
        public boolean equals (Object obj)
        {
            return obj == this;
        }
    }
}
