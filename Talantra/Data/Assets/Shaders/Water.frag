uniform sampler2D texture1;
uniform sampler2D texture2;
uniform float uni_blending;
in vec2 uv;

const vec3 fogColor = vec3(0.7);
const float fogDensity = 300.0;
const float LOG2 = 1.442695;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0;
	float f = clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0);
	vec4 color = mix(texture(texture1, uv), texture(texture2, uv), uni_blending);
	gl_FragColor = vec4(color.rgb, mix(1.0, color.a, f));
}