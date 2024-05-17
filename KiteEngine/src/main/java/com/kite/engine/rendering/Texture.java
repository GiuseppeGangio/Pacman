package com.kite.engine.rendering;

import com.kite.engine.core.Utils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.opengl.GL45.glBindTextureUnit;

public class Texture
{
    private int m_RendererID;
    private int m_Width, m_Height;

    public Texture(String filePath)
    {
        BufferedImage img = Utils.ImportImage(filePath);
        assert img != null : "IMAGE NOT FOUND!";
        img = Utils.FlipImageVertically(img);

        IntBuffer imgAsIntBuffer = Utils.ConvertImageToIntBuffer(img);

        m_Width = img.getWidth();
        m_Height = img.getHeight();

        m_RendererID = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(m_RendererID, 1, GL_RGBA8, m_Width, m_Height); // TODO: Support other formats than GL_RGBA8

        // TODO: Make setters
        {
            glTextureParameteri(m_RendererID, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTextureParameteri(m_RendererID, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTextureParameteri(m_RendererID, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTextureParameteri(m_RendererID, GL_TEXTURE_WRAP_T, GL_REPEAT);
        }

        glTextureSubImage2D(m_RendererID, 0, 0, 0, m_Width, m_Height, GL_RGBA, GL_UNSIGNED_BYTE, imgAsIntBuffer);

        imgAsIntBuffer.clear();
    }

    public Texture(int width, int height)
    {
        m_Width = width;
        m_Height = height;

        m_RendererID = glCreateTextures(GL_TEXTURE_2D);
        glTextureStorage2D(m_RendererID, 1, GL_RGBA8, m_Width, m_Height);

        glTextureParameteri(m_RendererID, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTextureParameteri(m_RendererID, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTextureParameteri(m_RendererID, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTextureParameteri(m_RendererID, GL_TEXTURE_WRAP_T, GL_REPEAT);
    }

    public Texture(int width, int height, int[] data)
    {
        this(width, height);
        SetData(data);
    }

    public void SetData(int[] data)
    {
        IntBuffer buffer = ByteBuffer.allocateDirect(data.length * 4)
                .order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(data).flip();
        glTextureSubImage2D(m_RendererID, 0, 0, 0, m_Width, m_Height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
    }

    public int GetWidth() { return m_Width; }
    public int GetHeight() { return m_Height; }
    public void Bind(int slot)
    {
        glBindTextureUnit(slot, m_RendererID);
    }

    protected int GetID () {return m_RendererID; }
}
