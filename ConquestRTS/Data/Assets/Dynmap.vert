uniform vec2 shift;
uniform float time;
uniform vec2 center;
out float percent;

void main(){
	float dis = distance(gl_Vertex.xy, center);
	float y = sin((time+dis)*0.01f)*100.0f;
	gl_Position = gl_ModelViewProjectionMatrix*vec4(gl_Vertex.x+shift.x, y, gl_Vertex.y+shift.y, 1.0f);
	percent = (y+100.0f)/200.0f;
}