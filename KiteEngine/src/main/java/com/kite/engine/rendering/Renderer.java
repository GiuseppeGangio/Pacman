package com.kite.engine.rendering;

import com.kite.engine.core.Application;
import com.kite.engine.core.Settings;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

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
}
