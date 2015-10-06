uniform sampler2D texture;
in vec2 uv;

const vec3 fogColor = vec3(0.7f);
const float fogDensity = 300.0f;
const float LOG2 = 1.442695f;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0f;
	float f = clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0);
	vec4 color = texture(texture, vec3(uv, 0.0f));
	gl_FragColor = vec4(color.rgb, mix(1.0f, color.a, f));
}