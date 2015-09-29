package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

class Vertex{
	private final float x;
	private final float y;
	private final float z;
	private final byte shade;
	private final float tx;
	private final float ty;
	private final byte texture;
	Vertex(float x, float y, float z, byte shade, float tx, float ty, byte texture){
		this.x = x;
		this.y = y;
		this.z = z;
		this.shade = shade;
		this.tx = tx;
		this.ty = ty;
		this.texture = texture;
	}
	public byte getTexture(){
		return texture;
	}
	public float getTx(){
		return tx;
	}
	public float getTy(){
		return ty;
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
	byte getShade(){
		return shade;
	}
}
