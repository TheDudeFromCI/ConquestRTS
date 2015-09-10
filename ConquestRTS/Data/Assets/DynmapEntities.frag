uniform sampler3D texture;
in float shade;
in vec3 uv;

void main(){
	gl_FragColor = vec4(texture(texture, uv).rgb*shade, 1.0f);
}