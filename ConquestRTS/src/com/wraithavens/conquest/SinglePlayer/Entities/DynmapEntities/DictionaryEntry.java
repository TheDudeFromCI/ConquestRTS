package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

class DictionaryEntry{
	private final float spawnChance;
	private final EntityType entity;
	private final int variations;
	DictionaryEntry(float spawnChance, EntityType entity, int variations){
		this.spawnChance = spawnChance;
		this.entity = entity;
		this.variations = variations;
	}
	EntityType getEntity(){
		return entity;
	}
	float getSpawnChance(){
		return spawnChance;
	}
	int getVariations(){
		return variations;
	}
}
