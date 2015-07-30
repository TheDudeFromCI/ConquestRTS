in float shade;
in float isGrass;
in vec2 shadowShift;
out vec3 color;
out vec3 position;
flat out float grass;
flat out vec2 shadowPos;

const float heightClamp = 1.0f/200.0f;
const float byteHeight = 1.0f/255.0f;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = mix(gl_Color.rgb, gl_Color.rgb*(gl_Vertex.y*heightClamp+0.5f), isGrass)*shade;
	grass = isGrass;
	position = gl_Vertex.xyz;
	position.y = (position.y+0.0001f)*byteHeight;
	shadowPos = shadowShift;
}