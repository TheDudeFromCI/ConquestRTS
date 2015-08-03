out vec3 color;
out vec3 normal;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = gl_Color.rgb;
	normal = gl_Normal;
}