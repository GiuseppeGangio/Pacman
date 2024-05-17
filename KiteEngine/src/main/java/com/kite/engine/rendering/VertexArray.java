package com.kite.engine.rendering;

import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class VertexArray
{
    private int m_RendererID;
    private VertexBuffer m_Vb;
    private IndexBuffer m_Ib;

    public VertexArray (VertexBuffer vb, IndexBuffer ib)
    {
        m_Vb = vb;
        m_Ib = ib;

        m_RendererID = glGenVertexArrays();
        glBindVertexArray(m_RendererID);

        vb.Bind();
        ib.Bind();

        if (vb.Layout != null)
        {
            int index = 0;
            for (VertexBufferLayoutElement element : vb.Layout.GetElements())
            {
                glEnableVertexAttribArray(index);
                glVertexAttribPointer(index,
                                      element.Count,
                                      element.GLType,
                                      element.Normalized,
                                      vb.Layout.GetStride(),
                                      element.Offset);
                index++;
            }
        }

        glBindVertexArray(0);
        vb.Unbind();
        ib.Unbind();
    }

    public VertexArray (VertexBuffer vb)
    {
        m_Vb = vb;
        m_Ib = null;

        m_RendererID = glGenVertexArrays();
        glBindVertexArray(m_RendererID);

        vb.Bind();

        if (vb.Layout != null)
        {
            int index = 0;
            for (VertexBufferLayoutElement element : vb.Layout.GetElements())
            {
                glEnableVertexAttribArray(index);
                glVertexAttribPointer(index,
                        element.Count,
                        element.GLType,
                        element.Normalized,
                        vb.Layout.GetStride(),
                        element.Offset);
                index++;
            }
        }

        glBindVertexArray(0);
        vb.Unbind();
    }

    public void Bind () { glBindVertexArray(m_RendererID); }
    public void Unbind () { glBindVertexArray(0); }

    public int GetIndicesCount() { return m_Ib != null ? m_Ib.GetIndicesCount() : 0; }

}
