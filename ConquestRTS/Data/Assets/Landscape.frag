flat in float color;

void main(){
	gl_FragColor = vec4(vec3(color)*vec3(0.0f, 0.7f, 0.0f), 1.0f);
}