uniform vec2 shift;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*vec4(gl_Vertex.x+shift.x, 0.0f, gl_Vertex.y+shift.y, 1.0f);
}