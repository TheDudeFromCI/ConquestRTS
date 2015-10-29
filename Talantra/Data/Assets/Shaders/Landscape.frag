#version 330
uniform sampler2D colorMap;
uniform sampler2DArray texture1;
in float edgeShade;
in vec2 pos;
in vec4 uv;
out vec4 fragColor;

const float textureScale =  1.0/63.0;
const vec3 fogColor = vec3(0.7);
const float fogDensity = 15.0;
const float LOG2 = 1.442695;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0;
	vec4 tex = texture(texture1, uv.xyz);
	fragColor = vec4(mix(fogColor, mix(vec3(1.0), texture(colorMap, floor(pos)*textureScale).rgb, tex.w)*tex.rgb*edgeShade, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), 1.0);
}