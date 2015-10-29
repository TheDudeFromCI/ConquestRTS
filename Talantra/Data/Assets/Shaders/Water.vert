uniform vec3 uni_offset;
in vec2 att_uv;
out vec2 uv;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*(gl_Vertex+vec4(uni_offset, 0.0));
	uv = att_uv;
}