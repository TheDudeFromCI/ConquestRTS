uniform sampler2D myTexture;
in vec2 texCoords;
in float shade;

void main(){
	vec4 c = texture2D(myTexture, texCoords);
	c.rgb *= shade;
    gl_FragColor = c;
}