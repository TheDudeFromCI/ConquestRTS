uniform vec2 offset;
in float shade;
flat out float edgeShade;
out vec2 pos;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	edgeShade = shade;
	pos = gl_Vertex.xz-offset;
}