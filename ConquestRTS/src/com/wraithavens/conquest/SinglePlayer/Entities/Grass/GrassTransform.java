package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

public class GrassTransform{
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	public GrassTransform(float x, float y, float z, float r, float s){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
	}
	public float getRotation(){
		return r;
	}
	public float getScale(){
		return s;
	}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
}
