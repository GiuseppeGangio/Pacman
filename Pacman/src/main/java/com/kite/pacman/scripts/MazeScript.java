package com.kite.pacman.scripts;

import com.kite.engine.core.Scene;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.rendering.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class MazeScript extends ScriptComponent
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
            1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1,
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

    private static final int FOOD_WIDTH = 26;
    private static final int FOOD_HEIGHT = 29;

    private static final int[] FOOD = new int[] {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1,
            1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1,
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
            0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
            1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    };

    private enum Wall
    {
        WALL_ONE_UP, WALL_ONE_DOWN, WALL_ONE_LEFT, WALL_ONE_RIGHT,
        WALL_TWO_HORIZONTAL, WALL_TWO_VERTICAL,
        WALL_THREE_UP, WALL_THREE_DOWN, WALL_THREE_LEFT, WALL_THREE_RIGHT,
        WALL_ANGLE_TOP_LEFT, WALL_ANGLE_TOP_RIGHT, WALL_ANGLE_BOTTOM_LEFT, WALL_ANGLE_BOTTOM_RIGHT,
    }

    public static final float PLAYER_SPAWN_OFFSET_X = 14 * MAZE_SCALE;
    public static final float PLAYER_SPAWN_OFFSET_Y = 23.5f * MAZE_SCALE;

    private final static Texture wallTexture1 = new Texture("assets/textures/wall1.png");
    private final static Texture wallTexture2 = new Texture("assets/textures/wall2.png");
    private final static Texture wallTexture3 = new Texture("assets/textures/wall3.png");
    private final static Texture wallTextureAngle = new Texture("assets/textures/wall_angle.png");

    private TransformComponent m_Transform;
    private Scene m_SceneRef;

    @Override
    public void OnAttach ()
    {
        m_Transform = entity.GetComponent(TransformComponent.class);
        m_SceneRef = entity.GetScene();
        CreateMaze();
        AddFood();

        SpawnPlayer();
    }

    private void SpawnPlayer ()
    {
        Entity player = m_SceneRef.CreateEntity("player");

        TransformComponent playerTransform = player.GetComponent(TransformComponent.class);
        playerTransform.SetParent(m_Transform);
        playerTransform.SetScale(MAZE_SCALE * 2f, MAZE_SCALE * 2f);
        playerTransform.SetPosition(PLAYER_SPAWN_OFFSET_X, -PLAYER_SPAWN_OFFSET_Y);

        RigidBodyComponent rbc = player.AddComponent(new RigidBodyComponent());
        player.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.ROTATIONAL_STATIC;

        player.AddComponent(new SpriteComponent());
        player.AddComponent(new PlayerScript());
    }

    private void AddFood ()
    {
        final float offset = 1.5f;

        for (int y = 0; y < FOOD_HEIGHT; y++)
        {
            for (int x = 0; x < FOOD_WIDTH; x++)
            {
                int index = x + y * FOOD_WIDTH;

                if (FOOD[index] == 1)
                    SpawnFood(offset + x, offset + y);
            }
        }
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
        Entity wall = m_SceneRef.CreateEntity("wall");

        TransformComponent wallTransform = wall.GetComponent(TransformComponent.class);
        wallTransform.SetParent(m_Transform);
        wallTransform.SetScale(MAZE_SCALE, MAZE_SCALE);
        wallTransform.SetPosition(x * MAZE_SCALE, -y * MAZE_SCALE);

        RigidBodyComponent rbc = wall.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = wall.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.STATIC;
        collider.Size = new Vector2f(0.5f, 0.5f);

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

    private void SpawnFood (float x, float y)
    {
        final float scale = 0.2f;

        Entity food = m_SceneRef.CreateEntity("food");

        TransformComponent foodTransform = food.GetComponent(TransformComponent.class);
        foodTransform.SetParent(m_Transform);
        foodTransform.SetScale(MAZE_SCALE * scale, MAZE_SCALE * scale);
        foodTransform.SetPosition(MAZE_SCALE * x,  MAZE_SCALE * -y);

        RigidBodyComponent rbc = food.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = food.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.STATIC;
        collider.Traversable = true;

        SpriteComponent foodSprite = food.AddComponent(new SpriteComponent());
        foodSprite.Sprite.Color = new Vector4f(1.0f, 0.7216f, 0.6f, 1.0f);

        food.AddComponent(new FoodScript());
    }
}
