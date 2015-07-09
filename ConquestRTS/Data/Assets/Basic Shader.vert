out vec2 texCoords;
out float shade;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	texCoords = gl_MultiTexCoord0.xy;
	if(gl_Normal.y==1.0f)shade = 1.0f;
	else if(gl_Normal.y==-1.0f)shade = 0.6f;
	else shade = 0.8f;
}