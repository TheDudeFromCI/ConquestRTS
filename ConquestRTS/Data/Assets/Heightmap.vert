uniform sampler2D texture;
out vec2 uv;

const float maxHeight = 250.0f;
const float textureSize = 64.0f;
const float inverseTextureSize = 1.0f/textureSize;

void main(){
	vec2 u = gl_MultiTexCoord0.xy;
	gl_Position = gl_ModelViewProjectionMatrix*(gl_Vertex+vec2(0.0f, texture(texture, (round(u*textureSize)+0.5f)*inverseTextureSize).a*maxHeight).xyxx);
	uv = u;
}