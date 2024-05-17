package com.kite.engine.rendering;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class VertexBuffer
{
    private int m_RendererID;
    VertexBufferLayout Layout;

    public VertexBuffer (float[] vertices, VertexBufferLayout layout)
    {
        Layout = layout;

        m_RendererID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, m_RendererID);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void Bind ()  { glBindBuffer(GL_ARRAY_BUFFER, m_RendererID); }
    public void Unbind() { glBindBuffer(GL_ARRAY_BUFFER, 0); }
}
