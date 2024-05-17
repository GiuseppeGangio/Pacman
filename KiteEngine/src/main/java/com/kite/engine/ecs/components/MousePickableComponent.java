package com.kite.engine.ecs.components;

import java.util.Random;

public class MousePickableComponent
{
    private int ID = s_Random.nextInt() | 0x000000ff;

    private static Random s_Random = new Random();

    public int GetID () { return ID; }

    @Override
    public boolean equals (Object obj)
    {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (obj instanceof MousePickableComponent)
            return ((MousePickableComponent) obj).ID == ID;

        return false;
    }

    @Override
    public int hashCode ()
    {
        return Integer.hashCode(ID);
    }
}
