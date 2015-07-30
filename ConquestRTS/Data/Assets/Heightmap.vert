uniform sampler2D texture;
out vec3 normal;

const float maxHeight = 250.0f;

void main(){
	vec2 u = gl_MultiTexCoord0.xy;
	vec4 map = texture2D(texture, u);
	gl_Position = gl_ModelViewProjectionMatrix*(gl_Vertex+vec4(0.0f, map.a*maxHeight, 0.0f, 0.0f));
	normal = map.rgb;
}