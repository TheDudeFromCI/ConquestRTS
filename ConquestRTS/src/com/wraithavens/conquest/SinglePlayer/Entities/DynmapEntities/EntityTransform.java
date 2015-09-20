package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class EntityTransform{
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	private final EntityType type;
	private int visibilityLevel;
	public EntityTransform(EntityType type, float x, float y, float z, float r, float s){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
		this.type = type;
	}
	public float getR(){
		return r;
	}
	public float getS(){
		return s;
	}
	public EntityType getType(){
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
	int getVisibilityLevel(){
		return visibilityLevel;
	}
	void setVisibilityLevel(int i){
		visibilityLevel = i;
	}
}
