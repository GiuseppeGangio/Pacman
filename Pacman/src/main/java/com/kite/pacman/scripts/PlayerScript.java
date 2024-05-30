package com.kite.pacman.scripts;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.SpriteComponent;
import com.kite.engine.event.Event;
import com.kite.engine.event.keyevents.KeyPressedEvent;
import com.kite.engine.input.InputKeys;
import com.kite.engine.rendering.Texture;

public class PlayerScript extends MovableEntityScript
{
    private static final Texture PLAYER_TEXTURE_OPEN = new Texture("assets/textures/player_open.png");
    private static final Texture PLAYER_TEXTURE_CLOSED = new Texture("assets/textures/player_closed.png");

    private SpriteComponent m_SpriteComponent;
    private NodeSystemScript.Direction m_NextDirection;

    @Override
    public void OnAttach ()
    {
        super.OnAttach();
        m_Speed = 2.5f;
        m_NextDirection = NodeSystemScript.Direction.NONE;

        m_SpriteComponent = entity.GetComponent(SpriteComponent.class);

        m_SpriteComponent.Sprite.Texture = PLAYER_TEXTURE_CLOSED;
        Time.StartTimer("TextureChange", 200);

        GameStateScript gameStateScript = entity.GetScene().GetEntity("GameState").GetComponent(GameStateScript.class);
    }

    @Override
    public void OnEvent (Event event)
    {
        Event.Dispatch(KeyPressedEvent.class, this::OnKeyPressedEvent, event);
    }

    @Override
    public void OnUpdate ()
    {
        UpdateSprite();
        if (Move(m_MovingDirection) && m_NextDirection != NodeSystemScript.Direction.NONE)
        {
            m_MovingDirection = m_NextDirection;
            m_NextDirection = NodeSystemScript.Direction.NONE;
        }
    }

    private boolean OnKeyPressedEvent (KeyPressedEvent event)
    {
        NodeSystemScript.Direction dir = NodeSystemScript.Direction.NONE;

        if (event.KeyCode() ==  InputKeys.KEY_W)
            dir = NodeSystemScript.Direction.UP;
        else if (event.KeyCode() ==  InputKeys.KEY_S)
            dir = NodeSystemScript.Direction.DOWN;
        else if (event.KeyCode() ==  InputKeys.KEY_A)
            dir = NodeSystemScript.Direction.LEFT;
        else if (event.KeyCode() ==  InputKeys.KEY_D)
            dir = NodeSystemScript.Direction.RIGHT;

        if (dir != NodeSystemScript.Direction.NONE && dir != m_MovingDirection && !ChangeDirection(dir))
        {
            if (SquaredDistanceToTarget() <= 2f)
                m_NextDirection = dir;
        }

        return false;
    }

    private void UpdateSprite ()
    {
        if (Time.HasTimerFinished("TextureChange"))
        {
            if (m_SpriteComponent.Sprite.Texture == PLAYER_TEXTURE_OPEN)
                m_SpriteComponent.Sprite.Texture = PLAYER_TEXTURE_CLOSED;
            else
                m_SpriteComponent.Sprite.Texture = PLAYER_TEXTURE_OPEN;

            Time.StartTimer("TextureChange", 200);
        }

        switch (m_MovingDirection)
        {
            case UP -> m_TransformComponent.SetRotation(90);
            case DOWN -> m_TransformComponent.SetRotation(-90);
            case LEFT -> m_TransformComponent.SetRotation(180);
            case RIGHT -> m_TransformComponent.SetRotation(0);
        }
    }
}
