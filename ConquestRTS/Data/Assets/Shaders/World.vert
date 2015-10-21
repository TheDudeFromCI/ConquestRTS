uniform vec3 chunkOffset;
in float att_shade;
in vec2 att_uv;
out float shade;
out vec2 biomePos;
out vec2 uv;

void main(){
	vec4 pos = gl_Vertex+vec4(chunkOffset, 0.0f);
	gl_Position = gl_ModelViewProjectionMatrix*pos;
	shade = att_shade;
	biomePos = gl_Vertex.xz-chunkOffset.xz;
	uv = att_uv;
}