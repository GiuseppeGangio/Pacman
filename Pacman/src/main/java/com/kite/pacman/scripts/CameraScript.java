package com.kite.pacman.scripts;

import com.kite.engine.core.Application;
import com.kite.engine.core.Window;
import com.kite.engine.ecs.components.CameraComponent;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;
import com.kite.engine.event.Event;
import com.kite.engine.event.windowevents.WindowResizeEvent;
import com.kite.engine.rendering.Camera;
import org.joml.Vector2f;

public class CameraScript extends ScriptComponent
{
    public float MovementSpeed, ZoomSpeed;

    private final Vector2f m_Position = new Vector2f();
    private float m_Zoom = 1.0f;

    private Camera m_CameraRef;

    @Override
    public void OnAttach ()
    {
        MovementSpeed = 0.1f;
        ZoomSpeed = 0.1f;

        m_CameraRef = entity.GetComponent(CameraComponent.class).Camera;

        Window windowRef = Application.Get().GetWindow();
        m_CameraRef.Resize(windowRef.GetWidth(), windowRef.GetHeight());

        final TransformComponent transform = entity.GetComponent(TransformComponent.class);
        transform.SetPosition(MazeScript.PLAYER_SPAWN_LOCATION_X, -MazeScript.PLAYER_SPAWN_LOCATION_Y / 2 - 2);
        m_CameraRef.Zoom(1/11f);
    }

    @Override
    public void OnEvent (Event event)
    {
        Event.Dispatch(WindowResizeEvent.class, this::OnWindowResizeEvent, event);
    }


    private boolean OnWindowResizeEvent (WindowResizeEvent event)
    {
        m_CameraRef.Resize(event.GetWidth(), event.GetHeight());
        return false;
    }
}
