uniform sampler2D texture;
uniform float alpha;
in vec2 uv;

void main(){
	vec4 color = texture(texture, uv);
	gl_FragColor = vec4(color.rgb, color.a*alpha);
}