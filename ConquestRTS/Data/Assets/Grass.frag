uniform sampler2D texture;
in vec2 uv;

void main(){
//	gl_FragColor = texture(texture, uv);
	gl_FragColor = vec4(texture(texture, uv).rgb, 1.0f);
}