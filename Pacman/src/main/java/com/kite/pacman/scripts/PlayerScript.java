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
    private static final Texture PLAYER_TEXTURE_OPEN = new Texture("assets/textures/player_open.png");
    private static final Texture PLAYER_TEXTURE_CLOSED = new Texture("assets/textures/player_closed.png");

    enum Direction
    {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    private SpriteComponent spriteComponent;
    private Direction direction = Direction.NONE;

    @Override
    public void OnAttach ()
    {
        spriteComponent = entity.GetComponent(SpriteComponent.class);

        spriteComponent.Sprite.Texture = PLAYER_TEXTURE_CLOSED;
        Time.StartTimer("TextureChange", 200);
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
            direction = Direction.UP;
        else if (Input.IsKeyPressed(InputKeys.KEY_S))
            direction = Direction.DOWN;
        else if (Input.IsKeyPressed(InputKeys.KEY_A))
            direction = Direction.LEFT;
        else if (Input.IsKeyPressed(InputKeys.KEY_D))
            direction = Direction.RIGHT;

        final TransformComponent transform = entity.GetComponent(TransformComponent.class);

        Vector2f position = transform.Position;
        float speed = 5 * MazeScript.MAZE_SCALE;
        float deltaTime = Time.DeltaTime() / 1000f;

        if (direction == Direction.UP)
        {
            position.y += speed * deltaTime;
            transform.SetRotation(90);
        } else if (direction == Direction.DOWN)
        {
            position.y -= speed * deltaTime;
            transform.SetRotation(-90);
        }
        else if (direction == Direction.LEFT)
        {
            position.x -= speed * deltaTime;
            transform.SetRotation(180);
        } else if (direction == Direction.RIGHT)
        {
            position.x += speed * deltaTime;
            transform.SetRotation(0);
        }

        transform.SetPosition(position.x, position.y);
    }
}
