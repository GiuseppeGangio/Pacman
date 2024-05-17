package com.kite.engine.rendering;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer
{
    private int m_RendererID = 0;

    public FrameBuffer ()
    {
        m_RendererID = glGenFramebuffers();
    }

    public void Bind () { glBindFramebuffer(GL_FRAMEBUFFER, m_RendererID); }
    public void Unbind() { glBindFramebuffer(GL_FRAMEBUFFER, 0); }

    public boolean IsComplete () { return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE; }

    public void AttachTexture (Texture texture, int attachmentType)
    {
        glFramebufferTexture2D(GL_FRAMEBUFFER, attachmentType, GL_TEXTURE_2D, texture.GetID(), 0);
    }

    public void Delete () { glDeleteFramebuffers(m_RendererID); }
}
