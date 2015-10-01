uniform sampler2D texture;
in vec2 uv;
in vec3 color;

const vec3 fogColor = vec3(0.7f);
const float fogDensity = 15.0f;
const float LOG2 = 1.442695f;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0f;
	vec4 col = texture(texture, uv);
	gl_FragColor = vec4(mix(fogColor, col.rgb*color, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), round(col.a));
}