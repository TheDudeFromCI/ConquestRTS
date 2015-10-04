package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

class GrassDataRaw{
	private final int type;
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	private final float red;
	private final float green;
	private final float blue;
	GrassDataRaw(int type, float x, float y, float z, float r, float s, float red, float green, float blue){
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
	float getBlue(){
		return blue;
	}
	float getGreen(){
		return green;
	}
	float getR(){
		return r;
	}
	float getRed(){
		return red;
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
