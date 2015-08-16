in float att_shade;
out vec3 tex;
flat out float shade;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	tex = gl_MultiTexCoord0.xyz;
	shade = att_shade;
}