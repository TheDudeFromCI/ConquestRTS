uniform vec3 sunDirection;
uniform sampler2D colors;
in vec3 normal;
in vec2 uv;

void main(){
	gl_FragColor = vec4(texture(colors, uv).rgb*(clamp(dot(normal, sunDirection), 0.0f, 1.0f)*0.5f+0.5f), 1.0f);
}