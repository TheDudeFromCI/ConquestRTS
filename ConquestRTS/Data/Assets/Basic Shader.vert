in float shade;
out vec3 color;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = gl_Color.rgb*shade;
}