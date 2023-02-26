#version 120
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform sampler2D u_skin;

void main() {
    vec4 v_skinCoords = texture2D(u_texture, v_texCoords)
    vec4 texColor = texture2D(u_skin, vec2(v_skinCoords.r, v_skinCoords.g));
    texColor.a = texColor.a * v_skinCoords.a;
    gl_FragColor = v_color * texColor;
}