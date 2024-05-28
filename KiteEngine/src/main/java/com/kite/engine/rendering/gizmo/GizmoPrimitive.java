package com.kite.engine.rendering.gizmo;

import org.joml.*;

import java.lang.Math;

public class GizmoPrimitive
{
    public enum Primitive
    {
        QUAD,
        LINE,
        VECTOR
    }

    private Primitive m_Primitive;
    private Matrix4f m_Transform;
    public Vector4f Color;
    public float Width;

    GizmoPrimitive[] Components; // For internal use, allows primitives to be formed of multiple components

    private static final Vector3f tmpVec3 = new Vector3f();
    private static final Quaternionf tmpQuaternion = new Quaternionf();

    public static GizmoPrimitive Quad (Vector2f center, Vector2f dimensions, Vector4f color)
    {
        Matrix4f transform = new Matrix4f()
                .translation(tmpVec3.set(center.x, center.y, 0.0f))
                .scale(tmpVec3.set(dimensions.x, dimensions.y, 1.0f));

        return new GizmoPrimitive(Primitive.QUAD, transform, 1.0f, color);
    }

    public static GizmoPrimitive Quad (Vector2f center, Vector2f dimensions)
    {
        return Quad(center, dimensions, new Vector4f(1.0f, 1.0f, 1.0f, 0.5f));
    }

    public static GizmoPrimitive Line (Vector2f point1, Vector2f point2, float width, Vector4f color)
    {
        Vector2f middlePoint = new Vector2f((point1.x + point2.x) / 2, (point1.y + point2.y) / 2);

        float xDistance = point1.x - point2.x;
        float yDistance = point1.y - point2.y;
        float distanceSquared = xDistance * xDistance + yDistance * yDistance;
        float length = (float) Math.sqrt(distanceSquared);

        double angle = xDistance == 0 ? Math.PI / 2 : Math.atan(yDistance / xDistance);

        Matrix4f transform = new Matrix4f()
                .translation(tmpVec3.set(middlePoint.x, middlePoint.y, 0.0f))
                .rotateZ((float) angle)
                .scale(length);

        return new GizmoPrimitive(Primitive.LINE, transform, width, color);
    }

    public static GizmoPrimitive Line (Vector2f point1, Vector2f point2)
    {
        return Line(point1, point2, 1.0f, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public static GizmoPrimitive Vector (Vector2f position, Vector2f vector, float maxLength, float width, Vector4f color)
    {
        Vector2f endPoint = new Vector2f(vector);
        float length = vector.length();

        if (maxLength >= 0 && length > maxLength)
        {
            Vector2f normalized = vector.normalize(new Vector2f());
            endPoint = normalized.mul(maxLength);
            length = maxLength;
        }

        endPoint.add(position);

        final Matrix4f positiveRotation = new Matrix4f().rotationZ((float) Math.PI / 6);
        final Matrix4f negativeRotation = new Matrix4f().rotationZ(-(float) Math.PI / 6);

        Vector2f back2d = position.sub(endPoint, new Vector2f()).normalize();
        Vector4f back4d = new Vector4f(back2d.x, back2d.y, 0.0f, 1.0f);

        Vector4f leftArrow4d = back4d.mul(positiveRotation, new Vector4f());//.normalize();
        Vector4f rightArrow4d = back4d.mul(negativeRotation, new Vector4f());//.normalize();

        Vector2f leftArrow = new Vector2f(leftArrow4d.x, leftArrow4d.y);
        Vector2f rightArrow = new Vector2f(rightArrow4d.x, rightArrow4d.y);

        leftArrow.normalize().mul(length * 0.2f);
        rightArrow.normalize().mul(length * 0.2f);

        GizmoPrimitive[] components = new GizmoPrimitive[] {
                Line(position, endPoint, width, color),
                Line(endPoint, endPoint.add(rightArrow, new Vector2f()), width, color),
                Line(endPoint, endPoint.add(leftArrow, new Vector2f()), width, color)
        };

        GizmoPrimitive primitive = new GizmoPrimitive(Primitive.VECTOR, null, width, color);
        primitive.Components = components;

        return primitive;
    }

    public static GizmoPrimitive Vector (Vector2f position, Vector2f vector, float maxLength)
    {
        return Vector(position, vector, maxLength, 1.0f, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    public static GizmoPrimitive Vector (Vector2f position, Vector2f vector)
    {
        return Vector(position, vector, -1f, 1.0f, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
    }

    private GizmoPrimitive (Primitive primitive, Matrix4f transform, float width, Vector4f color)
    {
        m_Primitive = primitive;
        m_Transform = transform;
        Color = color;
        Width = width;
    }

    public Primitive GetPrimitive () { return m_Primitive; }
    public Matrix4f GetTransform () { return m_Transform; }

    public GizmoPrimitive Color (Vector4f color)
    {
        Color.set(color);
        return this;
    }

    public GizmoPrimitive Color (float r, float g, float b, float a)
    {
        Color.set(r, g, b, a);
        return this;
    }
}
