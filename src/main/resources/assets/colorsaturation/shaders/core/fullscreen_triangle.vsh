#version 330
#extension GL_ARB_separate_shader_objects : require

layout(location = 0) out vec2 texCoord;

void main() {
    float x = -1.0 + float((gl_VertexID & 1) << 2);
    float y = -1.0 + float((gl_VertexID & 2) << 1);
    texCoord = (vec2(x, y) + 1.0) * 0.5;
    gl_Position = vec4(x, y, 0.0, 1.0);
}