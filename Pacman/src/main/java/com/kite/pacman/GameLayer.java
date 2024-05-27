package com.kite.pacman;

import com.kite.engine.core.*;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.CameraComponent;
import com.kite.pacman.scripts.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GameLayer extends Layer
{
    @Override
    protected void OnAttach ()
    {
        Settings.RendererSettings rendererSettings = Application.Get().GetSettings()._RendererSettings;
        rendererSettings.ClearColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

        Settings.PhysicsSettings physicsSettings = Application.Get().GetSettings()._PhysicsSettings;
        physicsSettings.Gravity = new Vector2f();

        SceneManager sceneManager = Application.Get().GetSceneManager();
        Scene sceneRef =  sceneManager.CreateEmptyScene("GameScene");
        sceneManager.SelectScene("GameScene");

        Application.Get().ReloadSettings();

        Entity camera = sceneRef.CreateEntity("Camera");
        camera.AddComponent(new CameraComponent());
        camera.AddComponent(new CameraScript());

        Entity gameState = sceneRef.CreateEntity("GameState");
        GameStateScript gameStateScript = gameState.AddComponent(new GameStateScript());

        Entity score = sceneRef.CreateEntity("Score");
        score.AddComponent(new ScoreScript());

        Entity livesCounter = sceneRef.CreateEntity("LivesCounter");
        livesCounter.AddComponent(new LivesCounterScript());

        Entity maze = sceneRef.CreateEntity("Maze");
        maze.AddComponent(new MazeScript());

        gameStateScript.InitializeGame();
    }

    @Override
    protected void OnUpdate ()
    {
        super.OnUpdate();
    }
}
