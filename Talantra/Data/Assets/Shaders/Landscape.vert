uniform vec3 offset;
in float shade;
in vec4 att_uv;
out float edgeShade;
out vec2 pos;
out vec4 uv;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*(gl_Vertex+vec4(offset, 0.0));
	edgeShade = shade;
	pos = gl_Vertex.xz-offset.xz;
	uv = att_uv;
}