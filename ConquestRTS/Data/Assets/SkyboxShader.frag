uniform samplerCube texture;
in vec3 uv;

void main(){
	gl_FragColor = textureCube(texture, normalize(uv));
}