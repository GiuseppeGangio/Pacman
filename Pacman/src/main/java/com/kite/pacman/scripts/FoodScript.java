package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.ScriptComponent;

public class FoodScript extends ScriptComponent
{
    private GameStateScript m_GameStateScript;

    @Override
    public void OnAttach ()
    {
        Entity gameState = entity.GetScene().GetEntity("GameState");
        m_GameStateScript = gameState.GetComponent(GameStateScript.class);
    }

    @Override
    public void OnCollision (Entity entity)
    {
        if (entity.HasComponent(PlayerScript.class))
        {
            m_GameStateScript.IncreaseScore(10);
        }
    }
}
