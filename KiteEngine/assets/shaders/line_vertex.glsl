#version 330 core

layout (location = 0) in vec3 v_Position;

uniform mat4 ViewProjMat;
uniform mat4 TransformationMat;

void main()
{
    gl_Position =  ViewProjMat * TransformationMat * vec4(v_Position, 1.0);
}