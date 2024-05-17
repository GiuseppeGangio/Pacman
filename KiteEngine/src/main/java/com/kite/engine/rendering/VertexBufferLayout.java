package com.kite.engine.rendering;

public class VertexBufferLayout
{
    private VertexBufferLayoutElement[] m_Elements;
    private int m_Stride;

    public VertexBufferLayout (VertexBufferLayoutElement... elements)
    {
        m_Elements = elements;
        m_Stride = 0;

        int offset = 0;
        for (int i = 0; i < m_Elements.length; i++)
        {
            m_Elements[i].Offset = offset;
            offset += m_Elements[i].GetSize();
            m_Stride += m_Elements[i].GetSize();
        }
    }

    public int GetStride() { return m_Stride; }
    public VertexBufferLayoutElement[] GetElements() { return m_Elements; }
}
