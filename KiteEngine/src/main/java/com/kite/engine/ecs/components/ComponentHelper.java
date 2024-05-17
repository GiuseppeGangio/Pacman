package com.kite.engine.ecs.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ComponentHelper
{
    private static final Vector3f s_TPos = new Vector3f();
    private static final Vector3f s_TScl = new Vector3f();
    private static final Quaternionf s_TRot = new Quaternionf();


    public static Matrix4f GetTransform (TransformComponent transform, Matrix4f mat)
    {
        s_TPos.set(transform.Position.x, transform.Position.y, 0.0f);
        s_TScl.set(transform.Scale.x, transform.Scale.y, 1.0f);
        s_TRot.rotationZ((float)Math.toRadians(transform.Rotation));

        mat.translationRotateScale(s_TPos, s_TRot, s_TScl);

        if (transform.GetParent() != null)
        {
            mat.mul(GetTransform(transform.GetParent(), new Matrix4f()));
        }

        return mat;
    }
}
