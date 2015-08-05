out vec2 uv;

const vec2 mad = vec2(0.5f);

void main(){
	gl_Position = vec4(gl_Vertex.xy, 0.0f, 1.0f);
	uv = gl_Vertex.xy*mad+mad;
}