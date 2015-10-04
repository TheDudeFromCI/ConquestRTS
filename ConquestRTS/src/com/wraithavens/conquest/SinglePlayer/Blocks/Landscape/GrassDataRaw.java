package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

public class GrassDataRaw{
	private final int type;
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	private final float red;
	private final float green;
	private final float blue;
	public GrassDataRaw(int type, float x, float y, float z, float r, float s, float red, float green, float blue){
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public float getBlue(){
		return blue;
	}
	public float getGreen(){
		return green;
	}
	public float getR(){
		return r;
	}
	public float getRed(){
		return red;
	}
	public float getS(){
		return s;
	}
	public int getType(){
		return type;
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
