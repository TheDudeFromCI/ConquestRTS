uniform sampler2D transform;
uniform int textureSize;
uniform int textureSizeHigh;
uniform float textureShrink;
flat out vec4 color;
in float shade;

void main(){
	int y = gl_InstanceID*4/textureSize;
	int x = (gl_InstanceID*4)&textureSizeHigh;
	gl_Position = gl_ModelViewProjectionMatrix*mat4(
		texture(transform, vec2(x, y)*textureShrink),
		texture(transform, vec2(x+1, y)*textureShrink),
		texture(transform, vec2(x+2, y)*textureShrink),
		texture(transform, vec2(x+3, y)*textureShrink)
	)*gl_Vertex;
	color = vec4(gl_Color.rgb*shade, 1.0f);
}