#version 330 core

in vec2 f_UV;

uniform vec4 u_Color;
uniform sampler2D u_Texture;

out vec4 FragColor;

void main()
{
    FragColor = u_Color * texture(u_Texture, f_UV);
}