uniform sampler3D texture;
flat in float color;
in vec3 pos;

const float textureScale =  1.0f/63.0f;

void main(){
	gl_FragColor = vec4(texture(texture, floor(pos)*textureScale).rgb*color, 1.0f);
}