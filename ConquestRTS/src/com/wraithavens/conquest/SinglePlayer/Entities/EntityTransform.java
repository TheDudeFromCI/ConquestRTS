package com.wraithavens.conquest.SinglePlayer.Entities;

public class EntityTransform{
	private float x;
	private float y;
	private float z;
	private float r;
	private float s;
	private EntityType type;
	public EntityTransform(EntityType type, float x, float y, float z, float r, float s){
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
}
