in vec3 att_offset;
in vec2 att_rotScale;
in float att_shade;
flat out vec3 color;

void main(){
	vec2 r = vec2(cos(att_rotScale.x), sin(att_rotScale.x));
	vec3 pos = vec3(
		gl_Vertex.x*r.x-gl_Vertex.z*r.y,
		gl_Vertex.y,
		gl_Vertex.x*r.y+gl_Vertex.z*r.x
	)*att_rotScale.y;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(pos+att_offset, 1.0f);
	color = gl_Color.rgb*att_shade;
}