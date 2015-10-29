#version 330
uniform int uni_colorBlended;
uniform sampler3D texture1;
in vec4 color;
in vec3 uv;
out vec4 fragColor;

const vec3 fogColor = vec3(0.7);
const float fogDensity = 15.0;
const float LOG2 = 1.442695;

void main(){
	float z = (gl_FragCoord.z/gl_FragCoord.w)/5000.0;
	float fogLevel = clamp(exp2(-fogDensity*fogDensity*z*z*LOG2), 0.0, 1.0);
	if(uni_colorBlended==1){
		fragColor = vec4(mix(fogColor, texture(texture1, uv).rgb*color.a, fogLevel), 1.0);
	}else{
		fragColor = vec4(mix(fogColor, color.rgb*color.a, fogLevel), 1.0);
	}
}