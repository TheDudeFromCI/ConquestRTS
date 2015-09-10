uniform vec3 uni_textureOffset;
uniform vec3 uni_textureSize;
in vec3 att_offset;
in vec2 att_rotScale;
in float att_shade;
out float shade;
out vec3 uv;

void main(){
	vec2 r = vec2(cos(att_rotScale.x), sin(att_rotScale.x));
	vec3 pos = vec3(
		gl_Vertex.x*r.x-gl_Vertex.z*r.y,
		gl_Vertex.y,
		gl_Vertex.x*r.y+gl_Vertex.z*r.x
	)*att_rotScale.y;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(pos+att_offset, 1.0f);
	uv = (gl_Vertex.xyz-uni_textureOffset)/uni_textureSize;
	shade = att_shade;
}