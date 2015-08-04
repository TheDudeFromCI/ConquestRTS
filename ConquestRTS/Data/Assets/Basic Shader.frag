uniform sampler2D grassShade;
flat in float grass;
in vec3 color;
in vec2 uv;

const float min = 1.0f/512.0f;
const float max = 511.0f/512.0f;

void main(){
	vec3 texColor = texture2D(grassShade, clamp(fract(uv), min, max)).rgb;
    gl_FragColor = vec4(mix(color, color*texColor, grass), 1.0f);
}