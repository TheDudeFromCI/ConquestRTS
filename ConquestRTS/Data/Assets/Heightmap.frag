in vec3 normal;

const vec3 lightDirection = vec3(0.35f, 0.3f, 0.35f);

void main(){
	gl_FragColor = vec4(vec3(clamp(dot(normal, lightDirection), 0.0f, 1.0f)), 1.0f);
}