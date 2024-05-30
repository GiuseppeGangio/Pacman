package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.rendering.Texture;

public class MazeVisualizerScript extends ScriptComponent
{
    public static final int MAZE_WIDTH = 29;
    public static final int MAZE_HEIGHT = 32;
    public static final float MAZE_SCALE = 0.5f;

    private static final int[] MAZE = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    private enum Wall
    {
        WALL_ONE_UP, WALL_ONE_DOWN, WALL_ONE_LEFT, WALL_ONE_RIGHT,
        WALL_TWO_HORIZONTAL, WALL_TWO_VERTICAL,
        WALL_THREE_UP, WALL_THREE_DOWN, WALL_THREE_LEFT, WALL_THREE_RIGHT,
        WALL_ANGLE_TOP_LEFT, WALL_ANGLE_TOP_RIGHT, WALL_ANGLE_BOTTOM_LEFT, WALL_ANGLE_BOTTOM_RIGHT,
    }

    private final static Texture wallTexture1 = new Texture("assets/textures/wall1.png");
    private final static Texture wallTexture2 = new Texture("assets/textures/wall2.png");
    private final static Texture wallTexture3 = new Texture("assets/textures/wall3.png");
    private final static Texture wallTextureAngle = new Texture("assets/textures/wall_angle.png");

    private TransformComponent m_Transform;

    @Override
    public void OnAttach ()
    {
        m_Transform = entity.GetComponent(TransformComponent.class);
        CreateMaze();
    }

    private void CreateMaze ()
    {
        for (int y = 0; y < MAZE_HEIGHT; y++)
        {
            for (int x = 0; x < MAZE_WIDTH; x++)
            {
                int index = x + y * MAZE_WIDTH;
                int wall = MAZE[index];

                if (wall == 1)
                {
                    Wall wall_type = Wall.WALL_ONE_UP;
                    int up = -1;
                    int down = -1;
                    int left = -1;
                    int right = -1;

                    if (y > 0)
                        up = MAZE[x + (y - 1) * MAZE_WIDTH];

                    if (y < MAZE_HEIGHT - 1)
                        down =  MAZE[x + (y + 1) * MAZE_WIDTH];

                    if (x > 0)
                        left = MAZE[(x - 1) + y * MAZE_WIDTH];

                    if (x < MAZE_WIDTH - 1)
                        right = MAZE[(x + 1) + y * MAZE_WIDTH];

                    int count = 0;

                    if (up == 1)
                        count++;

                    if (down == 1)
                        count++;

                    if (left == 1)
                        count++;

                    if (right == 1)
                        count++;

                    if (count == 1)
                    {
                        if (up == 1)
                            wall_type = Wall.WALL_ONE_UP;
                        else if (down == 1)
                            wall_type = Wall.WALL_ONE_DOWN;
                        else if (left == 1)
                            wall_type = Wall.WALL_ONE_LEFT;
                        else if (right == 1)
                            wall_type = Wall.WALL_ONE_RIGHT;
                    }

                    if (count == 2)
                    {
                        if (left == 1 && right == 1)
                            wall_type = Wall.WALL_TWO_HORIZONTAL;
                        else if (up == 1 && down == 1)
                            wall_type = Wall.WALL_TWO_VERTICAL;
                        else if (up == 1 && right == 1)
                            wall_type = Wall.WALL_ANGLE_BOTTOM_LEFT;
                        else if (up == 1 && left == 1)
                            wall_type = Wall.WALL_ANGLE_BOTTOM_RIGHT;
                        else if (down == 1 && right == 1)
                            wall_type = Wall.WALL_ANGLE_TOP_LEFT;
                        else if (down == 1 && left == 1)
                            wall_type = Wall.WALL_ANGLE_TOP_RIGHT;
                    }

                    if (count == 3)
                    {
                        if (up != 1)
                            wall_type = Wall.WALL_THREE_UP;
                        else if (down != 1)
                            wall_type = Wall.WALL_THREE_DOWN;
                        else if (left != 1)
                            wall_type = Wall.WALL_THREE_LEFT;
                        else if (right != 1)
                            wall_type = Wall.WALL_THREE_RIGHT;
                    }

                    CreateWall(x, y, wall_type);
                }

            }
        }
    }

    private void CreateWall (int x, int y, Wall wallType)
    {

        Entity wall = entity.GetScene().CreateEntity("wall");

        TransformComponent wallTransform = wall.GetComponent(TransformComponent.class);
        wallTransform.SetParent(m_Transform);
        wallTransform.SetScale(MAZE_SCALE, MAZE_SCALE);
        wallTransform.SetPosition(x * MAZE_SCALE, -y * MAZE_SCALE);

        SpriteComponent wallSprite = wall.AddComponent(new SpriteComponent());

        switch (wallType)
        {
            case WALL_ONE_UP ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(-90);
            }
            case WALL_ONE_DOWN ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(90);
            }
            case WALL_ONE_LEFT ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
            }
            case WALL_ONE_RIGHT ->
            {
                wallSprite.Sprite.Texture = wallTexture1;
                wallTransform.SetRotation(180);
            }
            case WALL_TWO_HORIZONTAL ->
            {
                wallSprite.Sprite.Texture = wallTexture2;
            }
            case WALL_TWO_VERTICAL ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTexture2;
            }
            case WALL_THREE_UP ->
            {
                wallTransform.SetRotation(180);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_DOWN ->
            {
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_LEFT ->
            {
                wallTransform.SetRotation(-90);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_THREE_RIGHT ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTexture3;
            }
            case WALL_ANGLE_TOP_LEFT ->
            {
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_TOP_RIGHT ->
            {
                wallTransform.SetRotation(-90);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_BOTTOM_LEFT ->
            {
                wallTransform.SetRotation(90);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
            case WALL_ANGLE_BOTTOM_RIGHT ->
            {
                wallTransform.SetRotation(180);
                wallSprite.Sprite.Texture = wallTextureAngle;
            }
        }
    }

}
