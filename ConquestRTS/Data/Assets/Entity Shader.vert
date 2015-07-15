in float weight;
flat out vec3 color;
//uniform vec3 lightDirection;
const vec3 lightDirection = vec3(0.3f, 0.4f, 0.3f);

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = gl_Color.rgb;
	float lightLevel = max(dot((gl_ModelViewProjectionMatrix*vec4(gl_Normal, 0.0f)).xyz, lightDirection), 0.0)*0.5f+0.5f;
	color *= lightLevel;
}