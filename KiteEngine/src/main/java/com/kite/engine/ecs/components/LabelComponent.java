package com.kite.engine.ecs.components;

import org.joml.Vector4f;

import java.awt.*;

public class LabelComponent
{
    public String Text = "";
    public Vector4f Color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public Font UsedFont = new Font("Arial", Font.PLAIN, 48);
}
