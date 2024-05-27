package com.kite.pacman;

import com.kite.engine.core.*;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.*;
import com.kite.engine.event.EventHandler;
import com.kite.engine.event.windowevents.WindowClosedEvent;
import com.kite.engine.input.Input;
import com.kite.pacman.scripts.*;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class PacmanLayer extends Layer
{
    public final static String MAIN_TITLE_SCENE = "MAIN_TITLE_SCENE";
    public final static String GAME_SCENE = "GAME_SCENE";

    private Entity m_StartButton;
    private Entity m_ExitButton;

    @Override
    protected void OnAttach ()
    {
        CreateMainTitleScene();
        CreateGameScene();
        SetSettings();

        SceneManager sceneManager = Application.Get().GetSceneManager();
        sceneManager.SelectScene(MAIN_TITLE_SCENE);
    }

    private void CreateMainTitleScene()
    {
        SceneManager sceneManager = Application.Get().GetSceneManager();
        Scene sceneRef =  sceneManager.CreateEmptyScene(MAIN_TITLE_SCENE);

        Entity camera = sceneRef.CreateEntity("Camera");
        camera.AddComponent(new CameraComponent());
        camera.AddComponent(new TitleCameraScript());

        {
            Entity gameTitle = sceneRef.CreateEntity("GameTitle");

            TransformComponent transformComponent = gameTitle.GetComponent(TransformComponent.class);
            transformComponent.SetScale(1, 0.3f);
            transformComponent.SetPosition(0, 0.7f);

            LabelComponent labelComponent = gameTitle.AddComponent(new LabelComponent());
            labelComponent.UsedFont = Common.GetFont();
            labelComponent.Text = "PACMAN";
        }

        {
            m_StartButton = sceneRef.CreateEntity("StartButton");

            TransformComponent transformComponent = m_StartButton.GetComponent(TransformComponent.class);
            transformComponent.SetScale(0.5f, 0.15f);
            transformComponent.SetPosition(0, 0.0f);

            SpriteComponent spriteComponent = m_StartButton.AddComponent(new SpriteComponent());
            spriteComponent.Sprite.Color = new Vector4f();

            LabelComponent labelComponent = m_StartButton.AddComponent(new LabelComponent());
            labelComponent.UsedFont = Common.GetFont();
            labelComponent.Text = "START";

            m_StartButton.AddComponent(new MousePickableComponent());
        }

        {
            m_ExitButton = sceneRef.CreateEntity("ExitButton");

            TransformComponent transformComponent = m_ExitButton.GetComponent(TransformComponent.class);
            transformComponent.SetScale(0.3f, 0.15f);
            transformComponent.SetPosition(0, -0.7f);

            SpriteComponent spriteComponent = m_ExitButton.AddComponent(new SpriteComponent());
            spriteComponent.Sprite.Color = new Vector4f();

            LabelComponent labelComponent = m_ExitButton.AddComponent(new LabelComponent());
            labelComponent.UsedFont = Common.GetFont();
            labelComponent.Text = "EXIT";

            m_ExitButton.AddComponent(new MousePickableComponent());
        }


    }

    @Override
    protected void OnUpdate ()
    {
        ManageHovering();
        ManageClick();
    }

    private void ManageClick ()
    {
        final SceneManager sceneManager = Application.Get().GetSceneManager();

        if (Input.IsMouseButtonPressed(0))
        {
            Scene scene = sceneManager.GetCurrentScene();
            int[] mousePos = Input.GetMousePosition();
            Entity entity = scene.MousePick(mousePos[0], mousePos[1]);
            if (entity.IsValid())
            {
                if (entity.GetScene() == m_StartButton.GetScene() && entity.GetId() == m_StartButton.GetId())
                    sceneManager.SelectScene(GAME_SCENE);
                else if (entity.GetScene() == m_ExitButton.GetScene() && entity.GetId() == m_ExitButton.GetId())
                    EventHandler.PropagateEvent(new WindowClosedEvent());
            }
        }

    }

    private void ManageHovering ()
    {
        m_StartButton.GetComponent(LabelComponent.class).Color = new Vector4f(1f, 1f, 1f, 1f);
        m_ExitButton.GetComponent(LabelComponent.class).Color = new Vector4f(1f, 1f, 1f, 1f);

        final SceneManager sceneManager = Application.Get().GetSceneManager();
        Scene scene = sceneManager.GetCurrentScene();

        int[] mousePos = Input.GetMousePosition();
        Entity entity = scene.MousePick(mousePos[0], mousePos[1]);
        if (entity.IsValid())
        {
            if (entity.GetScene() == m_StartButton.GetScene() && entity.GetId() == m_StartButton.GetId())
                m_StartButton.GetComponent(LabelComponent.class).Color = new Vector4f(1f, 0f, 0f, 1f);
            else if (entity.GetScene() == m_ExitButton.GetScene() && entity.GetId() == m_ExitButton.GetId())
                m_ExitButton.GetComponent(LabelComponent.class).Color = new Vector4f(1f, 0f, 0f, 1f);
        }
    }

    private void CreateGameScene()
    {
        SceneManager sceneManager = Application.Get().GetSceneManager();
        Scene sceneRef =  sceneManager.CreateEmptyScene(GAME_SCENE);

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

    private void SetSettings ()
    {
        Settings.RendererSettings rendererSettings = Application.Get().GetSettings()._RendererSettings;
        rendererSettings.ClearColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

        Settings.PhysicsSettings physicsSettings = Application.Get().GetSettings()._PhysicsSettings;
        physicsSettings.Gravity = new Vector2f();

        Application.Get().ReloadSettings();
    }
}
