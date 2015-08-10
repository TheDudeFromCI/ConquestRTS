package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

public class Vertex{
	private final float x;
	private final float y;
	private final float z;
	private final byte shade;
	public Vertex(float x, float y, float z, byte shade){
		this.x = x;
		this.y = y;
		this.z = z;
		this.shade = shade;
	}
	byte getShade(){
		return shade;
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
