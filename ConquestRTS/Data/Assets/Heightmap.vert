uniform sampler2D texture;
uniform vec3 mountainData;
out vec2 uv;

// Mountain Data:
// 0 = Texture Size
// 1 = Inverse Texture Size
// 2 = Max Height

void main(){
	vec2 u = gl_MultiTexCoord0.xy;
	gl_Position = gl_ModelViewProjectionMatrix*(gl_Vertex+vec2(0.0f, texture(texture, (round(u*mountainData.x)+0.5f)*mountainData.y).a*mountainData.z).xyxx);
	uv = u;
}