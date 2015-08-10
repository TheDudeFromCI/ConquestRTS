in float shade;
flat out float color;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = shade;
}