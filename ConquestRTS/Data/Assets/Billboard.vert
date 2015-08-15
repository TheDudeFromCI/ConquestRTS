out vec3 tex;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	tex = gl_MultiTexCoord0.xyz;
}