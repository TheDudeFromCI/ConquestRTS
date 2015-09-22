uniform int uni_colorBlended;
uniform sampler3D texture;
in vec3 color;
in float edgeShade;
in vec3 uv;

const vec3 fogColor = vec3(0.7f);
const float fogDensity = 15.0f;
const float LOG2 = 1.442695f;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0f;
	if(uni_colorBlended==1){
		gl_FragColor = vec4(mix(fogColor, texture(texture, uv).rgb*edgeShade, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), 1.0f);
	}else{
		gl_FragColor = vec4(mix(fogColor, color*edgeShade, clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0)), 1.0f);
	}
}