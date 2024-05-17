package com.kite.engine.input;

import com.kite.engine.core.Application;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public class Input
{
    private static boolean s_IsInputAllowed = true;

    public static void AllowInput (boolean allowed) { s_IsInputAllowed = allowed; }

    public static boolean IsKeyPressed (int keyCode)
    {
        long windowHandler = Application.Get().GetWindow().GetHandle();
        int value = glfwGetKey(windowHandler, keyCode);
        return s_IsInputAllowed && (value == GLFW_PRESS || value == GLFW_REPEAT);
    }

    public static boolean IsMouseButtonPressed (int button)
    {
        long windowHandler = Application.Get().GetWindow().GetHandle();
        int value = glfwGetMouseButton(windowHandler, button);
        return s_IsInputAllowed && (value == GLFW_PRESS);
    }

    public static int[] GetMousePosition()
    {
        long window = Application.Get().GetWindow().GetHandle();
        double[] xpos = {0}, ypos = {0};
        glfwGetCursorPos(window, xpos, ypos);
        return new int[] {
                (int) Math.floor(xpos[0]),
                (int) Math.floor(ypos[0])
        };
    }

    public static int GetMouseX() { return GetMousePosition()[0]; }
    public static int GetMouseY() { return GetMousePosition()[1]; }
}
