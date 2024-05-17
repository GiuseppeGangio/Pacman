package com.kite.engine.ecs.components;

import org.dyn4j.dynamics.PhysicsBody;

public class RigidBodyComponent
{
    public enum BodyType
    {
        DYNAMIC,
        STATIC, ROTATIONAL_STATIC, LINEARLY_STATIC
    }

    public BodyType Type = BodyType.DYNAMIC;
    public PhysicsBody BodyRef = null;

    public RigidBodyComponent ()
    {

    }

    public RigidBodyComponent (RigidBodyComponent other)
    {
        Type = other.Type;
        BodyRef = null;
    }

}
