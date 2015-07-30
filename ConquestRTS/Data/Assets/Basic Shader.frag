uniform sampler2D grassShade;
uniform sampler2D shadows;
flat in float grass;
in vec3 color;
in vec3 position;
flat in vec2 shadowPos;

const float invertedTextureSize = 1.0f/1024.0f;

void main(){
	vec3 newColor = mix(color, color*0.75f, step(position.y, texture2D(shadows, (floor(position.xz)+shadowPos)*invertedTextureSize).b));
    gl_FragColor = vec4(mix(newColor, newColor*texture2D(grassShade, floor(position.xz)*invertedTextureSize).rgb,
	grass), 1.0f);
}