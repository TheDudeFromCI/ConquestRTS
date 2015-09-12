uniform sampler2D colorMap;
flat in float edgeShade;
in vec2 pos;

const float textureScale =  1.0f/63.0f;

void main(){
	gl_FragColor = vec4(texture(colorMap, floor(pos)*textureScale).rgb*edgeShade, 1.0f);
}