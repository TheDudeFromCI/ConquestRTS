package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class EntityTransform{
	private float x;
	private float y;
	private float z;
	private float r;
	private float s;
	private EntityType type;
	private int visibilityLevel;
	public EntityTransform(EntityType type, float x, float y, float z, float r, float s, int visibilityLevel){
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
		this.s = s;
		this.type = type;
		this.visibilityLevel = visibilityLevel;
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
