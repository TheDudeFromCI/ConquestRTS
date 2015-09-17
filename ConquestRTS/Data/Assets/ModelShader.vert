uniform float uni_swayAmount;
uniform vec3 uni_meshCenter;
uniform float uni_time;
uniform vec3 uni_textureOffset;
uniform vec3 uni_textureSize;
uniform vec2 uni_rotScale;
out vec3 color;
in float shade;
out float edgeShade;
out vec3 uv;

void main(){
	vec2 r = vec2(cos(uni_rotScale.x), sin(uni_rotScale.x));
	vec3 pos = vec3(
		gl_Vertex.x*r.x-gl_Vertex.z*r.y,
		gl_Vertex.y,
		gl_Vertex.x*r.y+gl_Vertex.z*r.x
	)*uni_rotScale.y;
	uv = (gl_Vertex.xyz-uni_textureOffset)/uni_textureSize;
	vec2 swayLocations = vec2(sin(uni_time+uni_meshCenter.x), cos(uni_time+uni_meshCenter.z))*uni_swayAmount*pos.y;
	pos.x += swayLocations.x;
	pos.y -= (swayLocations.x+swayLocations.y)*0.2f;
	pos.z += swayLocations.y;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(pos+uni_meshCenter, 1.0f);
	edgeShade = shade;
	color = gl_Color.rgb;
}