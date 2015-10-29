#version 330
uniform sampler2D texture1;
in vec2 uv;
in vec3 color;
out vec4 fragColor;

const vec3 fogColor = vec3(0.7);
const float fogDensity = 15.0;
const float LOG2 = 1.442695;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0;
	vec4 col = texture(texture1, uv);
	fragColor = vec4(mix(fogColor, col.rgb*color, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), round(col.a));
}