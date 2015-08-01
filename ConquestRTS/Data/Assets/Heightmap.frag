uniform sampler2D texture;
uniform vec3 mountainData;
uniform vec3 sunDirection;
in vec2 uv;

const ivec3 off = ivec3(-1, 0, 1);

void main(){
	float z0 = textureOffset(texture, uv, off.xz).a;
	float z1 = textureOffset(texture, uv, off.yz).a;
	float z2 = textureOffset(texture, uv, off.zz).a;
	float z3 = textureOffset(texture, uv, off.xy).a;
	float z4 = textureOffset(texture, uv, off.zy).a;
	float z5 = textureOffset(texture, uv, off.xx).a;
	float z6 = textureOffset(texture, uv, off.yx).a;
	float z7 = textureOffset(texture, uv, off.zx).a;
	gl_FragColor = vec4(vec3(clamp(dot(normalize(vec3(z2+2.0f*z4+z7-z0-2.0f*z3-z5, 1.0f/mountainData.z, z5+2.0f*z6+z7-z0-2.0f*z1-z2)), sunDirection), 0.0f, 1.0f)*0.5f+0.5f)*texture(texture, uv+vec2(mountainData.y*0.5f)).rgb, 1.0f);
}