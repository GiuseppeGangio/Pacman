package com.kite.pacman.scripts;

import com.kite.engine.core.Application;
import com.kite.engine.core.Window;
import com.kite.engine.ecs.components.CameraComponent;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.event.Event;
import com.kite.engine.event.windowevents.WindowResizeEvent;
import com.kite.engine.rendering.Camera;

public class TitleCameraScript extends ScriptComponent
{
    private Camera m_CameraRef;

    @Override
    public void OnAttach ()
    {
        m_CameraRef = entity.GetComponent(CameraComponent.class).Camera;

        Window windowRef = Application.Get().GetWindow();
        m_CameraRef.Resize(windowRef.GetWidth(), windowRef.GetHeight());

        m_CameraRef.Zoom(1f);
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
