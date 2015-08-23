uniform sampler2D texture;
in vec2 uv;

void main(){
	vec4 col = texture(texture, uv);
//	gl_FragColor = vec4(1.0f, 0.5f, 0.0f, col.a);
	gl_FragColor = vec4(col.rgb, round(col.a));
}