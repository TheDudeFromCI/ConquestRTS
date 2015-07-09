varying vec4 vertColor;
varying vec2 texCoords;
uniform float time;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
    vertColor = gl_Color;
	texCoords = gl_MultiTexCoord0.xy;
}