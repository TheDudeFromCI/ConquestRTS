package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

class EntityDataRaw{
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
	float getR(){
		return r;
	}
	float getS(){
		return s;
	}
	int getType(){
		return type;
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
