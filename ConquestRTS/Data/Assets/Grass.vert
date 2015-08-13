uniform sampler2D transform;
uniform int textureSize;
uniform int textureSizeHigh;
uniform float textureShrink;

out vec2 uv;

void main(){
	gl_Position = gl_ModelViewProjectionMatrix*vec4(texture(transform, vec2(gl_InstanceID&textureSizeHigh, gl_InstanceID/textureSize)*textureShrink).xyz+gl_Vertex.xyz, 1.0f);
	uv = gl_MultiTexCoord0.xy;
}