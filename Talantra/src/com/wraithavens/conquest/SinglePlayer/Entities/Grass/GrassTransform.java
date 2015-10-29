package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

public class GrassTransform{
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	private final float red;
	private final float green;
	private final float blue;
	public GrassTransform(float x, float y, float z, float r, float s, float red, float green, float blue){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	float getBlue(){
		return blue;
	}
	float getGreen(){
		return green;
	}
	float getRed(){
		return red;
	}
	float getRotation(){
		return r;
	}
	float getScale(){
		return s;
	}
	float getX(){
		return x;
	}
	float getY(){
		return y;
	}
	float getZ(){
		return z;
	}
}
