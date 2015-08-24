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
	public byte getShade(){
		return shade;
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
