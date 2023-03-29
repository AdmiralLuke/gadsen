#version 120
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform float line_thickness;
uniform vec2 tex_size;
uniform vec4 v_skinBounds;
uniform vec4 outline_color;
uniform sampler2D u_texture;
uniform sampler2D u_skin;

void main() {
vec4 v_skinCoords = texture2D(u_texture, v_texCoords);
float a = 0;
vec2 size = vec2(1.0, 1.0) / tex_size * line_thickness;
if (v_skinCoords.a == 0.0) {
a += texture2D(u_texture, v_texCoords + vec2(size.x, 0)).a;
a += texture2D(u_texture, v_texCoords + vec2(size.x, size.y)).a;
a += texture2D(u_texture, v_texCoords + vec2(0, size.y)).a;
a += texture2D(u_texture, v_texCoords + vec2(- size.x, size.y)).a;
a += texture2D(u_texture, v_texCoords + vec2(size.x, -size.y)).a;
a += texture2D(u_texture, v_texCoords + vec2(- size.x, 0)).a;
a += texture2D(u_texture, v_texCoords + vec2(-size.x, - size.y)).a;
a += texture2D(u_texture, v_texCoords + vec2(0, - size.y)).a;
}

if (a > 0){
gl_FragColor = v_color * outline_color;
}else{
vec4 texColor = texture2D(u_skin, vec2(v_skinBounds[0] + v_skinCoords.r * v_skinBounds[2], v_skinBounds[1] + v_skinCoords.g * v_skinBounds[3]));
texColor.a = v_skinCoords.a;
gl_FragColor = v_color * texColor;
}

}