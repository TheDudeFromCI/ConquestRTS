uniform int uni_colorBlended;
uniform sampler3D texture;
in vec3 color;
in float edgeShade;
in vec3 uv;

void main(){
	if(uni_colorBlended==1){
		gl_FragColor = vec4(texture(texture, uv).rgb*edgeShade, 1.0f);
	}else{
		gl_FragColor = vec4(color*edgeShade, 1.0f);
	}
}