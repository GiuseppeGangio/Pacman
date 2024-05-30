package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.ScriptComponent;
import com.kite.engine.ecs.components.SpriteComponent;
import com.kite.engine.ecs.components.TransformComponent;
import com.kite.engine.rendering.Texture;

public class LivesCounterScript extends ScriptComponent
{
    private GameStateScript m_GameStateScript;
    private static final Texture LIFE_TEXTURE = new Texture("assets/textures/player_open.png");

    @Override
    public void OnAttach ()
    {
        Entity gameState = entity.GetScene().GetEntity("GameState");
        m_GameStateScript = gameState.GetComponent(GameStateScript.class);

        m_GameStateScript.Subscribe(GameStateScript.GameAction.PLAYER_KILLED, this::OnPlayerKilled);
        InitializeLives();
    }

    private void OnPlayerKilled (Void unused)
    {
        TransformComponent transformComponent = entity.GetComponent(TransformComponent.class);
        TransformComponent[] lives = transformComponent.GetChildren();

        if (lives.length > 0)
            lives[lives.length - 1].Entity.Delete();
    }

    private void InitializeLives ()
    {
        final float LIVE_SCALE =  2 * MazeVisualizerScript.MAZE_SCALE;
        final float OFFSET = MazeVisualizerScript.MAZE_SCALE * (MazeVisualizerScript.MAZE_WIDTH - 1);
        TransformComponent transformComponent = entity.GetComponent(TransformComponent.class);

        for (int i = 0; i < m_GameStateScript.PlayerLives(); i++)
        {
            Entity live = entity.GetScene().CreateEntity("live");

            TransformComponent liveTransform = live.GetComponent(TransformComponent.class);
            liveTransform.SetParent(transformComponent);
            liveTransform.SetScale(LIVE_SCALE, LIVE_SCALE);
            liveTransform.SetPosition(OFFSET - i * LIVE_SCALE, LIVE_SCALE);

            SpriteComponent spriteComponent = live.AddComponent(new SpriteComponent());
            spriteComponent.Sprite.Texture = LIFE_TEXTURE;
        }
    }
}
