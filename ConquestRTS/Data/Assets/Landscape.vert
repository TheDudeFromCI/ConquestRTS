uniform vec2 offset;
in float shade;
in vec3 att_uv;
flat out float edgeShade;
out vec2 pos;
out vec3 uv;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	edgeShade = shade;
	pos = gl_Vertex.xz-offset;
	uv = att_uv;
}