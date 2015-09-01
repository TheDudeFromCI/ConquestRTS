uniform int uni_giant;
uniform sampler3D texture;
uniform vec3 uni_textureSize;
in vec3 color;
in float edgeShade;
in vec3 uv;

void main(){
	if(uni_giant==1){
		vec3 c = texture(texture, uv).rgb;
		gl_FragColor = vec4(c*edgeShade, 1.0f);
	}else{
		gl_FragColor = vec4(color*edgeShade, 1.0f);
	}
}