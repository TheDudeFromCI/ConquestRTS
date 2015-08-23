uniform sampler2D texture;
in vec2 uv;

void main(){
	vec4 col = texture(texture, uv);
	gl_FragColor = vec4(col.rgb, round(col.a));
}