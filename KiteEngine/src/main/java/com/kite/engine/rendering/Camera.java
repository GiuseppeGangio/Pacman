package com.kite.engine.rendering;
import org.joml.Matrix4f;

public class Camera
{
    private Matrix4f m_ProjectionMatrix;

    private float m_AspectRatio, m_ZoomLevel;

    public Camera(float aspectRatio, float zoomLevel)
    {
        m_AspectRatio = aspectRatio;
        m_ZoomLevel = zoomLevel;

        m_ProjectionMatrix = new Matrix4f().setOrtho2D(
                -aspectRatio * zoomLevel,
                 aspectRatio * zoomLevel,
                -zoomLevel,
                 zoomLevel);
    }

    public Camera(float aspectRatio)
    {
        this(aspectRatio, 1.0f);
    }

    public void Resize (float width, float height)
    {
        m_AspectRatio = width / height;

        m_ProjectionMatrix = new Matrix4f().setOrtho2D(
                -m_AspectRatio * m_ZoomLevel,
                 m_AspectRatio * m_ZoomLevel,
                -m_ZoomLevel,
                 m_ZoomLevel);
    }

    public void Zoom (float zoom)
    {
        m_ZoomLevel = 1 / zoom;

        m_ProjectionMatrix = new Matrix4f().setOrtho2D(
                -m_AspectRatio * m_ZoomLevel,
                 m_AspectRatio * m_ZoomLevel,
                -m_ZoomLevel,
                 m_ZoomLevel);
    }

    public float GetZoom ()
    {
        return m_ZoomLevel;
    }

    public Matrix4f GetProjectionMatrix ()
    {
        return m_ProjectionMatrix;
    }
}
