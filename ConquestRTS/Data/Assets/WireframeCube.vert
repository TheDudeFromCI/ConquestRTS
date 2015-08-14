uniform vec3 color;
out vec3 fragColor;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	fragColor = color;
}