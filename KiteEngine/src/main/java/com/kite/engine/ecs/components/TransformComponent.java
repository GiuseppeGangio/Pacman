package com.kite.engine.ecs.components;

import org.joml.Vector2f;
import java.util.Vector;
import com.kite.engine.ecs.Entity;

public class TransformComponent
{
    public Entity Entity;

    public Vector2f Position = new Vector2f(0.0f, 0.0f);
    public float Rotation = 0;
    public Vector2f Scale = new Vector2f(1.0f, 1.0f);

    private TransformComponent m_Parent = null;
    private final Vector<TransformComponent> m_Children = new Vector<>();

    public TransformComponent () {}

    public TransformComponent (Vector2f position, float rotation, Vector2f scale)
    {
        Position = position;
        Rotation = rotation;
        Scale = scale;
    }

    public void SetPosition (float x, float y)
    {
        Position.set(x, y);

        RigidBodyComponent rbc = Entity.GetComponent(RigidBodyComponent.class);
        if (rbc != null && rbc.BodyRef != null)
        {
            rbc.BodyRef.getTransform().setTranslation(x, y);
            rbc.BodyRef.setAtRest(false);
        }
    }

    public void SetRotation (float rotation)
    {
        Rotation = rotation;

        RigidBodyComponent rbc = Entity.GetComponent(RigidBodyComponent.class);
        if (rbc != null && rbc.BodyRef != null)
        {
            rbc.BodyRef.getTransform().setRotation(Math.toRadians(rotation));
            rbc.BodyRef.setAtRest(false);
        }
    }

    public void SetScale (float x, float y)
    {
        Scale.set(x, y);
    }

    public void SetParent (TransformComponent parent)
    {
        if (parent != null)
        {
            m_Parent = parent;
            m_Parent.m_Children.add(this);
        }
        else if (m_Parent != null)
        {
            m_Parent.m_Children.remove(this);
            m_Parent = null;
        }
    }

    public Vector2f GetAbsolutePosition ()
    {
        Vector2f position = new Vector2f(Position);

        if (m_Parent != null)
        {
            position.add(m_Parent.GetAbsolutePosition());
        }

        return position;
    }

    public void SetAbsolutePosition (Vector2f position)
    {
        Vector2f parentPosition = m_Parent != null ? m_Parent.GetAbsolutePosition() : new Vector2f();
        Vector2f relativePosition = position.sub(parentPosition, new Vector2f());
        SetPosition(relativePosition.x, relativePosition.y);
    }

    public void SetAbsolutePosition (float x, float y) {SetAbsolutePosition(new Vector2f(x, y));}

    public TransformComponent GetParent ()
    {
        return m_Parent;
    }

    public TransformComponent[] GetChildren ()
    {
        return m_Children.toArray(new TransformComponent[0]);
    }

    @Override
    public boolean equals (Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (obj instanceof TransformComponent)
        {
            TransformComponent other = (TransformComponent) obj;
            return Entity.equals(other.Entity) &&
                    Position.equals(other.Position) &&
                    Scale.equals(other.Scale) &&
                    Rotation == other.Rotation &&
                    m_Parent.equals(other.m_Parent) &&
                    m_Children.equals(other.m_Children);
        }

        return false;
    }

    @Override
    public int hashCode ()
    {
        int hash = Entity.hashCode() ^ Position.hashCode() ^ Scale.hashCode() ^ Float.hashCode(Rotation) ^ m_Children.hashCode();

        if (m_Parent != null)
            hash ^= m_Parent.hashCode();

        return hash;
    }

    @Override
    public String toString ()
    {
        return "TransformComponent: \n" +
                "\t Position: " + Position + "\n" +
                "\t Rotation: " + Rotation + "\n" +
                "\t Scale: " + Scale;
    }
}
