package com.kite.pacman;

import com.kite.engine.core.Application;
import com.kite.engine.core.Layer;
import com.kite.engine.core.Scene;
import com.kite.engine.ecs.Entity;
import com.kite.engine.ecs.components.CameraComponent;
import com.kite.engine.ecs.components.SpriteComponent;
import com.kite.pacman.scripts.CameraScript;

public class GameLayer extends Layer
{
    @Override
    protected void OnAttach ()
    {
        Scene sceneRef = Application.Get().GetScene();

        Entity camera = sceneRef.CreateEntity("Camera");
        camera.AddComponent(new CameraComponent());
        camera.AddComponent(new CameraScript());

        Entity quad = sceneRef.CreateEntity("Quad");
        quad.AddComponent(new SpriteComponent());
    }

    @Override
    protected void OnUpdate ()
    {
        super.OnUpdate();
    }
}
