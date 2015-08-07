flat out vec4 color;

const vec3 lightDirection = vec3(0.35f, 0.3f, 0.35f);
const float lightPower = 100.0f;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	float lightLevel = max(dot((gl_ModelViewProjectionMatrix*vec4(gl_Normal, 0.0f)).xyz, lightDirection*lightPower), 0.0)*0.25f+0.75f;
	color = vec4(gl_Color.rgb*lightLevel, 1.0f);
}