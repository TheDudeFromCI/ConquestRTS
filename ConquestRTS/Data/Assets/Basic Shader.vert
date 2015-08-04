in float shade;
in float isGrass;
out vec3 color;
out vec2 uv;
out vec3 pos;
flat out float grass;

const float textureSize1 = 1.0f/512.0f;

void main(){
    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
	color = gl_Color.rgb*shade;
	grass = isGrass;
	uv = gl_Vertex.xz*textureSize1;
	pos = gl_Vertex.xyz*10.0f;
}