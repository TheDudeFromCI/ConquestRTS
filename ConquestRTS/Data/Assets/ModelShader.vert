flat out vec4 color;
in float shade;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = vec4(gl_Color.rgb*shade, 1.0f);
}