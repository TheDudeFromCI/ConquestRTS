uniform sampler2D texture;
uniform vec2 shift;
uniform vec2 size;

void main(){
	vec2 uv = gl_Vertex.xy/size;
	float height = 0.0f;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(gl_Vertex.x+shift.x, height, gl_Vertex.y+shift.y, 1.0f);
}