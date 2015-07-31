uniform sampler2D texture;
in vec2 uv;

//Actually this is the opposite of the direction, but, whatever.
const vec3 lightDirection = vec3(0.35f, 0.3f, 0.35f);
const vec2 size = vec2(2.0f, 0.0f);
const ivec3 off = ivec3(-1, 0, 1);
const float maxHeight = 250.0f;
const float textureSize = 64.0f;
const float inverseTextureSize = 1.0f/textureSize;

void main(){
	float zy = textureOffset(texture, uv, off.zy).a*maxHeight;
	float xy = textureOffset(texture, uv, off.xy).a*maxHeight;
	float yz = textureOffset(texture, uv, off.yz).a*maxHeight;
	float yx = textureOffset(texture, uv, off.yx).a*maxHeight;
	vec3 hor = normalize(vec3(size.xy, zy-xy));
	vec3 ver = normalize(vec3(size.yx, yz-yx));
	vec3 color = texture(texture, uv+vec2(inverseTextureSize*0.5f)).rgb;
	float lightLevel = clamp(dot(cross(hor, ver), lightDirection), 0.0f, 1.0f)*0.5f+0.5f;
	gl_FragColor = vec4(vec3(lightLevel)*color, 1.0f);
}