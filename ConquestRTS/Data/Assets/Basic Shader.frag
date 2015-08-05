uniform sampler2D grassShade;
flat in float grass;
in vec3 color;
in vec2 uv;
in vec3 pos;

const float min = 1.0f/512.0f;
const float max = 511.0f/512.0f;

void main(){
    gl_FragColor = vec4(mix(color, color*texture2D(grassShade, clamp(fract(uv), min, max)).rgb, grass), 1.0f);
}