package com.kite.engine.rendering;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class IndexBuffer
{
    private int m_RendererID;
    private int m_IndicesCount;

    public IndexBuffer (int... indices)
    {
        m_IndicesCount = indices.length;

        m_RendererID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void Bind() { glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, m_RendererID); }
    public void Unbind() { glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0); }

    public int GetIndicesCount () { return m_IndicesCount; }
}
