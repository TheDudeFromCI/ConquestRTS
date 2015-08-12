uniform sampler2D transform;
flat out vec4 color;
in float shade;

const float shrink = 1.0f/64.0f;

void main(){
	int y = gl_InstanceID*4/64;
	int x = (gl_InstanceID*4)&63;
	gl_Position = gl_ModelViewProjectionMatrix*mat4(
		texture(transform, vec2(x, y)*shrink),
		texture(transform, vec2(x+1, y)*shrink),
		texture(transform, vec2(x+2, y)*shrink),
		texture(transform, vec2(x+3, y)*shrink)
	)*gl_Vertex;
	color = vec4(gl_Color.rgb*shade, 1.0f);
}