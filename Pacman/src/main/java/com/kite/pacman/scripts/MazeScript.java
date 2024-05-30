package com.kite.pacman.scripts;

import com.kite.engine.core.Scene;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;

import static com.kite.pacman.scripts.MazeVisualizerScript.MAZE_SCALE;

public class MazeScript extends ScriptComponent
{
    public static final float PLAYER_SPAWN_LOCATION_X = 14 * MAZE_SCALE;
    public static final float PLAYER_SPAWN_LOCATION_Y = 23.5f * MAZE_SCALE;
    public static final float PLAYER_SCALE = MAZE_SCALE * 1.75f;

    private TransformComponent m_Transform;
    private Scene m_SceneRef;

    @Override
    public void OnAttach ()
    {
        m_Transform = entity.GetComponent(TransformComponent.class);
        m_SceneRef = entity.GetScene();
        CreateMaze();
        CreateFoodSystem();

        SpawnPlayer();
    }

    private void CreateMaze ()
    {
        Entity maze = m_SceneRef.CreateEntity("Maze");

        TransformComponent transformComponent = maze.GetComponent(TransformComponent.class);
        transformComponent.SetParent(m_Transform);

        maze.AddComponent(new MazeVisualizerScript());
    }

    private void CreateFoodSystem ()
    {
        Entity foodSystem = m_SceneRef.CreateEntity("FoodSystem");

        TransformComponent transformComponent = foodSystem.GetComponent(TransformComponent.class);
        transformComponent.SetParent(m_Transform);

        foodSystem.AddComponent(new FoodSystemScript());
    }

    private void SpawnPlayer ()
    {
        Entity player = m_SceneRef.CreateEntity("player");

        TransformComponent playerTransform = player.GetComponent(TransformComponent.class);
        playerTransform.SetParent(m_Transform);
        playerTransform.SetScale(PLAYER_SCALE, PLAYER_SCALE);
        playerTransform.SetPosition(PLAYER_SPAWN_LOCATION_X, -PLAYER_SPAWN_LOCATION_Y);

        RigidBodyComponent rbc = player.AddComponent(new RigidBodyComponent());
        ColliderComponent collider = player.AddComponent(new ColliderComponent());
        rbc.Type = RigidBodyComponent.BodyType.ROTATIONAL_STATIC;
        collider.Traversable = true;

        player.AddComponent(new SpriteComponent());
        player.AddComponent(new PlayerScript());
    }
}
