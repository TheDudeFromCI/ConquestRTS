uniform sampler2D transform;
uniform uvec2 uni_texStats;
uniform float time;
in float swayTolerance;
out vec2 uv;

const float swayStrength = 0.15f;

void main(){
	vec4 tex = texture(transform, vec2(gl_InstanceID&uni_texStats.y, gl_InstanceID/uni_texStats.x)/float(uni_texStats.x));
	vec2 r = vec2(cos(tex.a), sin(tex.a));
	vec3 pos = vec3(
		gl_Vertex.x*r.x-gl_Vertex.z*r.y,
		gl_Vertex.y,
		gl_Vertex.x*r.y+gl_Vertex.z*r.x
	);
	vec2 swayLocations = vec2(sin(time+pos.x), cos(+pos.z))*swayStrength*swayTolerance;
	pos.x += swayLocations.x;
	pos.y -= swayLocations.x+swayLocations.y;
	pos.z += swayLocations.y;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(tex.xyz+pos, 1.0f);
	uv = gl_MultiTexCoord0.xy;
}