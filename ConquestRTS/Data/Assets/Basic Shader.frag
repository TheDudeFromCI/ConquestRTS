uniform sampler2D grassShade;
flat in float grass;
in vec3 color;
in vec2 uv;

const float invertedTextureSize = 1.0f/1024.0f;

void main(){
	vec3 texColor = texture2D(grassShade, floor(uv)*invertedTextureSize).rgb;
	texColor = vec3(1.0f);
    gl_FragColor = vec4(mix(color, color*texColor, grass), 1.0f);
}