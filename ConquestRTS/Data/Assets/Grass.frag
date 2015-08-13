uniform sampler2D texture;
in vec2 uv;

void main(){
	gl_FragColor = texture(texture, uv);
}