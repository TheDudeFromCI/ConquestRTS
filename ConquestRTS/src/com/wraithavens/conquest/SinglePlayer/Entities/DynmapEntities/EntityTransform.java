package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

class EntityTransform{
	private float x;
	private float y;
	private float z;
	private float r;
	private float s;
	private int visibilityLevel;
	EntityTransform(float x, float y, float z, float r, float s, int visibilityLevel){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
		this.visibilityLevel = visibilityLevel;
	}
	float getRotation(){
		return r;
	}
	float getScale(){
		return s;
	}
	int getVisibilityLevel(){
		return visibilityLevel;
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
	void setVisibilityLevel(int i){
		visibilityLevel = i;
	}
}
