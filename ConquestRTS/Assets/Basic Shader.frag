uniform sampler2D myTexture;
varying vec4 vertColor;
varying vec2 texCoords;

void main(){
	vec4 c = texture2D(myTexture, texCoords);
    gl_FragColor = vertColor*c;
}