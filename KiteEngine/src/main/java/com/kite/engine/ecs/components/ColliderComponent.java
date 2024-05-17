package com.kite.engine.ecs.components;

import org.joml.Vector2f;

public class ColliderComponent
{
    public Vector2f Offset = new Vector2f();
    public Vector2f Size = new Vector2f(1f, 1f);

    public boolean Traversable = false;

    public float Density = 1.0f;
    public float Friction = 0.5f;
    public float Restitution = 0.0f;
    public float RestitutionThreshold = 1f;

    public long FilterCategory = 1;
    public long FilterMask = Long.MAX_VALUE;

    public ColliderComponent ()
    {

    }

    public ColliderComponent (ColliderComponent other)
    {
        Offset = other.Offset;
        Size = other.Size;
        Traversable = other.Traversable;
        Density = other.Density;
        Friction = other.Friction;
        Restitution = other.Restitution;
        RestitutionThreshold = other.RestitutionThreshold;
        FilterCategory = other.FilterCategory;
        FilterMask = other.FilterMask;
    }
}
