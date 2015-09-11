package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class GiantEntityListEntry{
	private final int x;
	private final int y;
	private final int z;
	private final EntityType type;
	GiantEntityListEntry(int x, int y, int z, EntityType type){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	EntityType getType(){
		return type;
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
}
