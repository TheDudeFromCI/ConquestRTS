uniform sampler3D texture;
in vec3 tex;
flat in float shade;

void main(){
	vec4 color = texture(texture, tex);
	gl_FragColor = vec4(color.rgb*shade, round(color.a));
}