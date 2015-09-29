uniform sampler2D colorMap;
uniform sampler2DArray texture;
flat in float edgeShade;
in vec2 pos;
in vec3 uv;

const float textureScale =  1.0f/63.0f;
const vec3 fogColor = vec3(0.7f);
const float fogDensity = 15.0f;
const float LOG2 = 1.442695f;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0f;
	vec3 col = texture(colorMap, floor(pos)*textureScale).rgb*texture(texture, uv).rgb;
	gl_FragColor = vec4(mix(fogColor, col*edgeShade, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), 1.0f);
}