package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.TransformComponent;
import org.joml.Vector2f;

public class TeleportScript extends ScriptComponent
{
    private Vector2f m_TpLoc = null;

    @Override
    public void OnCollision (Entity entity)
    {
        if (m_TpLoc != null && entity.HasComponent(PlayerScript.class))
        {
            TransformComponent playerTransform = entity.GetComponent(TransformComponent.class);
            playerTransform.SetPosition(m_TpLoc.x, m_TpLoc.y);
        }
    }

    public void setTeleportLocation (float x, float y)
    {
        m_TpLoc = new Vector2f(x, y);
    }
}
