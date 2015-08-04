uniform sampler2D grassShade;
flat in float grass;
in vec3 color;
in vec2 uv;

void main(){
	vec3 texColor = texture2D(grassShade, uv).rgb;
    gl_FragColor = vec4(mix(color, color*texColor, grass), 1.0f);
}