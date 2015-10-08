package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

public class EntityDataRaw{
	private final int type;
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	EntityDataRaw(int type, float x, float y, float z, float r, float s){
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
	}
	public float getR(){
		return r;
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
