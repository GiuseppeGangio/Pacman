package com.kite.engine.core;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Settings
{
    public ApplicationSettings _ApplicationSettings = new ApplicationSettings();
    public RendererSettings _RendererSettings = new RendererSettings();
    public PhysicsSettings _PhysicsSettings = new PhysicsSettings();

    public static class ApplicationSettings
    {
        public int WindowWidth = 1280, WindowHeight = 720;
        public String WindowTitle = "";
    }

    public static class RendererSettings
    {
        public Vector4f ClearColor = new Vector4f(1f, 0f, 1f, 1.0f);
        public boolean EnableBlending = true;
    }

    public static class PhysicsSettings
    {
        public double StepFrequency = 1d / 60d;
        public int VelocityConstraintSolverIterations = 6;
        public int PositionConstraintSolverIterations = 2;
        public ContinuousDetectionModeEnum ContinuousDetectionMode = ContinuousDetectionModeEnum.ALL;

        public Vector2f Gravity = new Vector2f(0, -9.8f);

        public enum ContinuousDetectionModeEnum
        {
            NONE, BULLETS_ONLY, ALL
        }
    }
}
