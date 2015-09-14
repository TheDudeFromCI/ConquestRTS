package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class GiantEntityListEntry{
	private final float x;
	private final float y;
	private final float z;
	private final float r;
	private final float s;
	private final EntityType type;
	GiantEntityListEntry(EntityType type, float x, float y, float z, float r, float s){
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
	EntityType getType(){
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
