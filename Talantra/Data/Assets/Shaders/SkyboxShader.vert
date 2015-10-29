out vec3 uv;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	uv = gl_Vertex.xyz;
}