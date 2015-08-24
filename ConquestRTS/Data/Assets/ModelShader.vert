uniform float uni_swayAmount;
uniform vec2 uni_meshCenter;
uniform float uni_time;
flat out vec4 color;
in float shade;

void main(){
	vec4 pos = gl_Vertex;
	vec2 swayLocations = vec2(
		sin(uni_time+uni_meshCenter.x),
		cos(uni_time+uni_meshCenter.y)
	)*uni_swayAmount*pos.y;
	pos.x += swayLocations.x;
	pos.y -= (swayLocations.x+swayLocations.y)*0.2f;
	pos.z += swayLocations.y;
	gl_Position = gl_ModelViewProjectionMatrix*pos;
	color = vec4(gl_Color.rgb*shade, 1.0f);
}