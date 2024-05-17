package com.kite.engine.core;

import com.kite.engine.event.Event;

import java.util.Vector;

final class LayerStack
{
    private final Vector<Layer> m_Stack;

    LayerStack ()
    {
        m_Stack = new Vector<>();
    }

    void AddLayer (Layer layer)
    {
        layer.OnAttach();
        m_Stack.add(layer);
    }

    void PropagateEvent (Event event)
    {
        int index = m_Stack.size() - 1;

        while (!event.Handled && index >= 0)
        {
            m_Stack.get(index).OnEvent(event);
            index--;
        }
    }

    void Run ()
    {
        m_Stack.forEach((Layer layer) ->
        {
            if (layer.m_Active)
                layer.OnUpdate();
        });
    }
}
