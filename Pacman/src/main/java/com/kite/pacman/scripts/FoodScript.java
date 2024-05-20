package com.kite.pacman.scripts;

import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.ScriptComponent;

public class FoodScript extends ScriptComponent
{
    private ScoreScript m_ScoreScript;

    @Override
    public void OnAttach ()
    {
        Entity scoreEntity = entity.GetScene().GetEntity("Score");
        m_ScoreScript = scoreEntity.GetComponent(ScoreScript.class);
    }

    @Override
    public void OnCollision (Entity entity)
    {
        if (entity.HasComponent(PlayerScript.class))
        {
            m_ScoreScript.AddScore(10);
        }
    }
}
