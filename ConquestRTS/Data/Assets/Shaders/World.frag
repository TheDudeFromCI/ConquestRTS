#version 330
uniform sampler2D biomeColorTexture;
uniform sampler2DArray blockTexture;
in float shade;
in vec2 biomePos;
in vec4 uv;
out vec4 fragColor;

const float textureScale =  1.0/31.0;
const vec3 fogColor = vec3(0.7);
const float fogDensity = 15.0;
const float LOG2 = 1.442695;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0;
	vec4 tex = texture(blockTexture, uv.xyz);
	fragColor = vec4(mix(fogColor, mix(vec3(1.0), texture(biomeColorTexture, floor(biomePos)*textureScale).rgb, tex.w)*tex.rgb*shade, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), 1.0);
}