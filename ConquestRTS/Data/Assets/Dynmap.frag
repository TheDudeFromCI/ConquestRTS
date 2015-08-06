uniform vec3 sunDirection;
in vec3 color;
in vec3 normal;

void main(){
	gl_FragColor = vec4(color*(clamp(dot(normal, sunDirection), 0.0f, 1.0f)*0.5f+0.5f), 1.0f);
}