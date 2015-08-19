uniform vec3 offset;
in float shade;
flat out float color;
out vec3 pos;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = shade;
	pos = gl_Vertex.xyz-offset;
}