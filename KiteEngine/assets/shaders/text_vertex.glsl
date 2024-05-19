#version 330 core

layout (location = 0) in vec3 v_Position;
layout (location = 1) in vec2 v_UV;

uniform mat4 ViewProjMat;
uniform mat4 TransformationMat;

out vec2 f_UV;

void main()
{
    f_UV = v_UV;
    gl_Position =  ViewProjMat * TransformationMat * vec4(v_Position, 1.0);
}