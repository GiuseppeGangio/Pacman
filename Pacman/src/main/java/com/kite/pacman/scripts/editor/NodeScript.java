package com.kite.pacman.scripts.editor;

import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;
import com.kite.engine.event.Event;
import com.kite.engine.event.keyevents.KeyPressedEvent;
import com.kite.engine.input.InputKeys;
import org.joml.Vector2f;

import java.util.Random;

public class NodeScript extends ScriptComponent
{
    enum NodeType
    {
        DEFAULT, GATE
    }

    boolean Movable = false;
    NodeType Type = NodeType.DEFAULT;
    private float m_Spacing = 1;
    private int m_ID = -1;

    public NodeScript Up = null, Down = null, Left = null, Right = null;

    @Override
    public void OnAttach ()
    {
        final Random random = new Random();
        m_ID = random.nextInt(Integer.MAX_VALUE);
    }

    public void SetSpacing (float spacing)
    {
        m_Spacing = spacing;
    }
    public int GetID () { return m_ID; }

    public void SetID (int id) { m_ID = id; }

    public Vector2f GetPosition () { return entity.GetComponent(TransformComponent.class).Position; }

    public void CreateUpDownConnection (NodeScript down, boolean removeIfPresent)
    {
        if (m_ID == down.m_ID)
            return;

        if (removeIfPresent && Down != null && Down.GetID() == down.GetID() && down.Up != null && down.Up.GetID() == m_ID)
        {
            // Delete connection
            Down = null;
            down.Up = null;
            return;
        }

        Down = down;
        down.Up = this;
    }

    public void CreateUpDownConnection (NodeScript down) { CreateUpDownConnection(down, true); }

    public void CreateLeftRightConnection (NodeScript right, boolean removeIfPresent)
    {
        if (m_ID == right.m_ID)
            return;

        if (removeIfPresent && Right != null && Right.GetID() == right.GetID() && right.Left != null && right.Left.GetID() == m_ID)
        {
            // Delete connection
            Right = null;
            right.Left = null;
            return;
        }

        Right = right;
        right.Left = this;
    }

    public void CreateLeftRightConnection (NodeScript right) {CreateLeftRightConnection(right, true);}

    @Override
    public void OnUpdate ()
    {


    }

    @Override
    public void OnEvent (Event event)
    {
        Event.Dispatch(KeyPressedEvent.class, this::OnKeyPressedEvent, event);
    }

    private boolean OnKeyPressedEvent (KeyPressedEvent event)
    {
        if (!Movable)
            return false;

        TransformComponent transform = entity.GetComponent(TransformComponent.class);

        if (transform == null)
            return false;

        if (event.KeyCode() == InputKeys.KEY_UP)
        {
            transform.Position.y += m_Spacing;
            return true;
        }
        else if (event.KeyCode() == InputKeys.KEY_DOWN)
        {
            transform.Position.y -= m_Spacing;
            return true;
        }

        if (event.KeyCode() == InputKeys.KEY_RIGHT)
        {
            transform.Position.x += m_Spacing;
            return true;
        }
        else if (event.KeyCode() == InputKeys.KEY_LEFT)
        {
            transform.Position.x -= m_Spacing;
            return true;
        }

        return false;
    }
}
