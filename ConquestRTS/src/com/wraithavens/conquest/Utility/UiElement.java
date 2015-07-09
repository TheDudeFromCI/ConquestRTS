package com.wraithavens.conquest.Utility;

import org.lwjgl.opengl.GL11;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

class UiElement{
	Texture texture;
	public float x;
	public float y;
	public float w;
	public float h;
	public float r;
	public float a = 1;
	UiElement(Texture texture){
		this.texture = texture;
	}
	private void drawPoint(float x, float y, float sin, float cos){
		x -= this.x;
		y -= this.y;
		float newX = x*cos-y*sin;
		float newY = x*sin+y*cos;
		x = newX+this.x;
		y = newY+this.y;
		GL11.glVertex3f(x, y, 0);
	}
	void render(){
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		float halfWidth = w/2;
		float halfHeight = h/2;
		float x1 = x-halfWidth;
		float y1 = y-halfHeight;
		float x2 = x+halfWidth;
		float y2 = y+halfHeight;
		float sin = (float)Math.sin(r);
		float cos = (float)Math.cos(r);
		GL11.glColor4f(1, 1, 1, a);
		GL11.glTexCoord2f(0, 1);
		drawPoint(x1, y1, sin, cos);
		GL11.glTexCoord2f(1, 1);
		drawPoint(x2, y1, sin, cos);
		GL11.glTexCoord2f(1, 0);
		drawPoint(x2, y2, sin, cos);
		GL11.glTexCoord2f(0, 0);
		drawPoint(x1, y2, sin, cos);
		GL11.glEnd();
	}
}