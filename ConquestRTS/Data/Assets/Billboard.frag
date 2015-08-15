uniform sampler3D texture;
in vec3 tex;

void main(){
	vec4 color = texture(texture, tex);
	gl_FragColor = vec4(color.rgb, round(color.a));
}