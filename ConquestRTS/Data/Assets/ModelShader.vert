uniform float uni_swayAmount;
uniform vec2 uni_meshCenter;
uniform float uni_time;
uniform vec3 uni_textureOffset;
uniform vec3 uni_textureSize;
out vec3 color;
in float shade;
out float edgeShade;
out vec3 uv;

void main(){
	vec4 pos = gl_Vertex;
	uv = (pos.xyz-uni_textureOffset)/uni_textureSize;
	vec2 swayLocations = vec2(
		sin(uni_time+uni_meshCenter.x),
		cos(uni_time+uni_meshCenter.y)
	)*uni_swayAmount*pos.y;
	pos.x += swayLocations.x;
	pos.y -= (swayLocations.x+swayLocations.y)*0.2f;
	pos.z += swayLocations.y;
	gl_Position = gl_ModelViewProjectionMatrix*pos;
	edgeShade = shade;
	color = gl_Color.rgb;
}