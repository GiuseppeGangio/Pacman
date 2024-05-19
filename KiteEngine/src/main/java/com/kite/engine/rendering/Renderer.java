package com.kite.engine.rendering;

import com.kite.engine.core.Application;
import com.kite.engine.core.Settings;

import com.kite.engine.core.Utils;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;

public class Renderer
{
    public static class QuadData
    {
        static VertexArray s_VAO;
        static Shader s_Shader;

        static void Init ()
        {
            float[] vertices = {
                     0.5f,  0.5f, 0.0f,  1, 1, // top right
                     0.5f, -0.5f, 0.0f,  1, 0, // bottom right
                    -0.5f, -0.5f, 0.0f,  0, 0, // bottom left
                    -0.5f,  0.5f, 0.0f,  0, 1  // top left
            };

            int[] indices = {
                    0, 1, 3,   // first triangle
                    1, 2, 3    // second triangle
            };

            VertexBuffer vb = new VertexBuffer(vertices, new VertexBufferLayout(
                    new VertexBufferLayoutElement(GL_FLOAT, 3), // position
                    new VertexBufferLayoutElement(GL_FLOAT, 2)  // uv
            ));

            IndexBuffer ib = new IndexBuffer(indices);
            s_VAO = new VertexArray(vb, ib);

            s_Shader = new Shader(
                    "assets/shaders/quad_vertex.glsl",
                    "assets/shaders/quad_fragment.glsl");
        }
    }

    public static class LineData
    {
        static VertexArray s_VAO;
        static Shader s_Shader;

        static void Init ()
        {
            float[] vertices = {
                    -0.5f,  0.0f, 0.0f, // left
                     0.5f,  0.0f, 0.0f  // right
            };

            VertexBuffer vb = new VertexBuffer(vertices, new VertexBufferLayout(
                    new VertexBufferLayoutElement(GL_FLOAT, 3) // position
            ));

            s_VAO = new VertexArray(vb);

            s_Shader = new Shader(
                    "assets/shaders/line_vertex.glsl",
                    "assets/shaders/line_fragment.glsl");
        }
    }

    private static class TextData
    {
        public static HashMap<String, Texture> s_BufferedTexts = new HashMap<>();
    }

    private final static Matrix4f s_ViewProjectionMatrix = new Matrix4f();
    private static Settings.RendererSettings s_Settings = new Settings.RendererSettings();

    public static void Init ()
    {
        GL.createCapabilities();
        ReloadSettings();
        QuadData.Init();
        LineData.Init();
    }

    public static void ReloadSettings ()
    {
        s_Settings = Application.Get().GetSettings()._RendererSettings;

        if (s_Settings.EnableBlending)
        {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        glClearColor(s_Settings.ClearColor.x,
                     s_Settings.ClearColor.y,
                     s_Settings.ClearColor.z,
                     s_Settings.ClearColor.w);
    }

    public static void SetViewportSize (int width, int height)
    {
        glViewport(0, 0, width, height);
    }

    public static void Clear ()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public static void StartScene (Matrix4f viewMatrix, Matrix4f projectionMatrix)
    {
        projectionMatrix.mul(viewMatrix.invert(new Matrix4f()), s_ViewProjectionMatrix);
    }

    public static void EndScene()
    {

    }

    public static void Render (Matrix4f transform, Sprite sprite)
    {
        RenderQuad(transform, sprite.Color, sprite.Texture);
    }

    public static void RenderQuad (Matrix4f transform, Vector4f color, Texture texture)
    {
        QuadData.s_VAO.Bind();
        QuadData.s_Shader.Bind();
        QuadData.s_Shader.SetUniformMat4("ViewProjMat", s_ViewProjectionMatrix);

        QuadData.s_Shader.SetUniformMat4("TransformationMat", transform);
        QuadData.s_Shader.SetUniformVec4("u_Color", color);
        texture.Bind(0);

        glDrawElements(GL_TRIANGLES, QuadData.s_VAO.GetIndicesCount(), GL_UNSIGNED_INT, 0);

        QuadData.s_Shader.Unbind();
        QuadData.s_VAO.Unbind();
    }

    public static void RenderLine (Matrix4f transform, float width, Vector4f color)
    {
        LineData.s_VAO.Bind();
        LineData.s_Shader.Bind();
        LineData.s_Shader.SetUniformMat4("ViewProjMat", s_ViewProjectionMatrix);

        LineData.s_Shader.SetUniformMat4("TransformationMat", transform);
        LineData.s_Shader.SetUniformVec4("u_Color", color);

        glLineWidth(width);
        glDrawArrays(GL_LINES, 0, 2);

        LineData.s_Shader.Unbind();
        LineData.s_VAO.Unbind();
    }

    // for now, we deal with text as a textured quad
    public static void RenderText (Matrix4f transform, Vector4f color, Font font, String text)
    {
        Texture texture = GetQuadTextureFromText(font, text);
        RenderQuad(transform, color, texture);
    }

    public static void RenderText (Matrix4f trasform, Font font, String text)
    {
        RenderText(trasform, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), font, text);
    }

    private static Texture GetQuadTextureFromText (Font font, String text)
    {
        Texture tex = TextData.s_BufferedTexts.get(text);

        if (tex != null)
            return tex;

        tex = CreateQuadTextureFromText(font, text);
        TextData.s_BufferedTexts.put(text, tex);
        return tex;
    }

    private static Texture CreateQuadTextureFromText (Font font, String text)
    {
        BufferedImage img = CreateImageFromText(font, text);
        BufferedImage flippedImg = Utils.FlipImageVertically(img);

        Texture texture = new Texture(img.getWidth(), img.getHeight());
        texture.SetData(Utils.ConvertImageToIntArray(flippedImg));

        return texture;
    }

    // See https://stackoverflow.com/a/18800845
    private static BufferedImage CreateImageFromText (Font font, String text)
    {
        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);

        fm = g2d.getFontMetrics();

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        return img;
    }
}
