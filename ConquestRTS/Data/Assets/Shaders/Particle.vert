in vec3 att_offset;
in vec2 att_scale;
in vec4 att_color;
out vec4 color;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*vec4(att_offset+vec3(gl_ModelViewMatrix[0][0], gl_ModelViewMatrix[1][0], gl_ModelViewMatrix[2][0])*gl_Vertex.x*att_scale.x+vec3(gl_ModelViewMatrix[0][1], gl_ModelViewMatrix[1][1], gl_ModelViewMatrix[2][1])*gl_Vertex.y*att_scale.y, 1.0);
	color = att_color;
}