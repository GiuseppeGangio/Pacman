package com.kite.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_FLOAT;

public class VertexBufferLayoutElement
{
    public int Count;
    public int GLType;
    public boolean Normalized;
    public int Offset;

    public VertexBufferLayoutElement (int glType, int count, boolean normalized)
    {
        GLType = glType;
        Count = count;
        Normalized = normalized;
    }

    public VertexBufferLayoutElement (int glType, int count)
    {
        this(glType, count, true);
    }

    public int GetSize ()
    {
        return Count * GetSizeBasedOnType();
    }

    private int GetSizeBasedOnType ()
    {
        // TODO: Support more than 1 type
        switch(GLType)
        {
            case GL_FLOAT : return 4;
        }

        return 0;
    }
}
