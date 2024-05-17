package com.kite.engine.rendering;

import com.kite.engine.core.Utils;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int m_RendererID;

    public Shader (String vertex_shader_path, String fragment_shader_path)
    {
        String vertexShaderSource   = Utils.ReadFile(vertex_shader_path);
        String fragmentShaderSource = Utils.ReadFile(fragment_shader_path);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderSource);
        glCompileShader(vertexShader);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);

        m_RendererID = glCreateProgram();

        glAttachShader(m_RendererID, vertexShader);
        glAttachShader(m_RendererID, fragmentShader);
        glLinkProgram(m_RendererID);

        glUseProgram(m_RendererID);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void Bind () { glUseProgram(m_RendererID); }

    public void Unbind () { glUseProgram(0); }

    public void SetUniformMat4 (String name, Matrix4f value)
    {
        float[] buffer = new float[16];
        value.get(buffer);

        int loc = glGetUniformLocation(m_RendererID, name);
        glUniformMatrix4fv(loc, false, buffer);
    }

    public void SetUniformVec4 (String name, Vector4f value)
    {
        int loc = glGetUniformLocation(m_RendererID, name);
        glUniform4f(loc, value.x, value.y, value.z, value.w);
    }
}
