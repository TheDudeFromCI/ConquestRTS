in float shade;
in float isGrass;
out vec3 color;
out vec2 uv;
flat out float grass;

const float heightClamp = 1.0f/200.0f;
const float byteHeight = 1.0f/255.0f;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = gl_Color.rgb*shade;
	grass = isGrass;
	uv = gl_Vertex.xz/1024.0f;
}