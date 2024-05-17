package com.kite.engine.rendering;

import org.joml.Vector4f;

public class Sprite
{
    public Vector4f Color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public Texture Texture = s_WhiteTexture;

    final static private Texture s_WhiteTexture = new Texture(1, 1, new int[] {0xffffffff});

    public static Texture WhiteTexture () { return s_WhiteTexture; }
}
