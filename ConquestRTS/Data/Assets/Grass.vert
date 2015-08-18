uniform sampler2D transform;
uniform int textureSize;
uniform int textureSizeHigh;
uniform float textureShrink;

out vec2 uv;

void main(){
	vec4 tex = texture(transform, vec2(gl_InstanceID&textureSizeHigh, gl_InstanceID/textureSize)*textureShrink);
	vec2 r = vec2(cos(tex.a), sin(tex.a));
	vec3 pos = vec3(
		gl_Vertex.x*r.x-gl_Vertex.z*r.y,
		gl_Vertex.y,
		gl_Vertex.x*r.y+gl_Vertex.z*r.x
	);
	gl_Position = gl_ModelViewProjectionMatrix*vec4(tex.xyz+pos, 1.0f);
	uv = gl_MultiTexCoord0.xy;
}