package com.kite.pacman.scripts;

import com.kite.engine.core.Time;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.SpriteComponent;
import com.kite.engine.ecs.components.TransformComponent;
import com.kite.engine.input.Input;
import com.kite.engine.input.InputKeys;
import com.kite.engine.rendering.Texture;
import org.joml.Vector2f;

public class PlayerScript extends ScriptComponent
{
    public enum Direction
    {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    private static final Texture PLAYER_TEXTURE_OPEN = new Texture("assets/textures/player_open.png");
    private static final Texture PLAYER_TEXTURE_CLOSED = new Texture("assets/textures/player_closed.png");

    private float m_Speed = 5 * MazeScript.MAZE_SCALE, m_PreviousSpeed;

    private SpriteComponent spriteComponent;
    private Direction m_Direction = Direction.NONE, m_PreviousDirection;

    @Override
    public void OnAttach ()
    {
        spriteComponent = entity.GetComponent(SpriteComponent.class);

        spriteComponent.Sprite.Texture = PLAYER_TEXTURE_CLOSED;
        Time.StartTimer("TextureChange", 200);

        GameStateScript gameStateScript = entity.GetScene().GetEntity("GameState").GetComponent(GameStateScript.class);
        gameStateScript.Subscribe(GameStateScript.GameAction.GAME_PAUSE_START, (Void unused) ->
        {
            m_PreviousSpeed = m_Speed;
            m_PreviousDirection = m_Direction;

            m_Speed = 0;
            m_Direction = Direction.NONE;
        });

        gameStateScript.Subscribe(GameStateScript.GameAction.GAME_PAUSE_STOP, (Void unused) ->
        {
            m_Speed = m_PreviousSpeed;
            m_Direction = m_PreviousDirection;
        });

        gameStateScript.Subscribe(GameStateScript.GameAction.PLAYER_KILLED, (Void unused)->
        {
            m_Direction = Direction.NONE;
            entity.GetComponent(TransformComponent.class).SetPosition(MazeScript.PLAYER_SPAWN_LOCATION_X, -MazeScript.PLAYER_SPAWN_LOCATION_Y);
        });
    }

    @Override
    public void OnCollision (Entity entity)
    {
        if (entity.HasComponent(FoodScript.class))
        {
            entity.Delete();
        }
    }

    @Override
    public void OnUpdate ()
    {
        if (Time.HasTimerFinished("TextureChange"))
        {
            if (spriteComponent.Sprite.Texture == PLAYER_TEXTURE_OPEN)
                spriteComponent.Sprite.Texture = PLAYER_TEXTURE_CLOSED;
            else
                spriteComponent.Sprite.Texture = PLAYER_TEXTURE_OPEN;

            Time.StartTimer("TextureChange", 200);
        }

        if (Input.IsKeyPressed(InputKeys.KEY_W))
            m_Direction = Direction.UP;
        else if (Input.IsKeyPressed(InputKeys.KEY_S))
            m_Direction = Direction.DOWN;
        else if (Input.IsKeyPressed(InputKeys.KEY_A))
            m_Direction = Direction.LEFT;
        else if (Input.IsKeyPressed(InputKeys.KEY_D))
            m_Direction = Direction.RIGHT;

        final TransformComponent transform = entity.GetComponent(TransformComponent.class);

        Vector2f position = transform.Position;
        float deltaTime = Time.DeltaTime() / 1000f;

        if (m_Direction == Direction.UP)
        {
            position.y += m_Speed * deltaTime;
            transform.SetRotation(90);
        } else if (m_Direction == Direction.DOWN)
        {
            position.y -= m_Speed * deltaTime;
            transform.SetRotation(-90);
        }
        else if (m_Direction == Direction.LEFT)
        {
            position.x -= m_Speed * deltaTime;
            transform.SetRotation(180);
        } else if (m_Direction == Direction.RIGHT)
        {
            position.x += m_Speed * deltaTime;
            transform.SetRotation(0);
        }

        transform.SetPosition(position.x, position.y);
    }

    public float Speed () { return m_Speed; }
    public Direction Direction () { return m_Direction;}
}
