package com.kite.pacman.scripts;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.rendering.Texture;
import org.joml.Vector2f;

import java.util.ArrayList;

public class BlinkyScript extends ScriptComponent
{
    TransformComponent m_Transform;
    SpriteComponent m_SpriteComponent;

    MazeScript m_MazeScript;
    TransformComponent m_PlayerTransform;

    ArrayList<Vector2f> m_PreviousPath;

    @Override
    public void OnAttach ()
    {
        m_SpriteComponent = entity.GetComponent(SpriteComponent.class);
        m_SpriteComponent.Sprite.Texture = new Texture("assets/textures/blinky.png");
        m_Transform = entity.GetComponent(TransformComponent.class);

        Entity player = entity.GetScene().GetEntity("player");
        m_PlayerTransform = player.GetComponent(TransformComponent.class);
    }

    @Override
    public void OnUpdate ()
    {
        if (m_MazeScript == null)
        {
            m_MazeScript = m_Transform.GetParent().Entity.GetComponent(MazeScript.class);
        }

        float speed = 4 * MazeScript.MAZE_SCALE;
        float deltaTime = Time.DeltaTime() / 1000f;

        ArrayList<Vector2f> path = m_MazeScript.PathFind(m_Transform.Position, m_PlayerTransform.Position);

        if (!path.isEmpty())
        {
            m_PreviousPath = path;
        }

        if (m_PreviousPath != null && !m_PreviousPath.isEmpty())
        {
            Vector2f direction = m_PreviousPath.get(0);
            Vector2f position = m_Transform.Position;

            position.add(direction.mul(speed * deltaTime));
            m_Transform.SetPosition(position.x, position.y);

            m_PreviousPath.remove(0);
        }
    }
}
