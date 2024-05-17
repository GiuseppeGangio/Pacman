package com.kite.engine.ecs.components;

import com.kite.engine.rendering.Camera;

public class CameraComponent
{
    public Camera Camera;

    public CameraComponent () { Camera = new Camera(1, 1); }
}
