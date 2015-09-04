package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class DictionaryEntry{
	private final double spawnChance;
	private final EntityType entity;
	private final int variations;
	DictionaryEntry(double spawnChance, EntityType entity, int variations){
		this.spawnChance = spawnChance;
		this.entity = entity;
		this.variations = variations;
	}
	EntityType getEntity(){
		return entity;
	}
	double getSpawnChance(){
		return spawnChance;
	}
	int getVariations(){
		return variations;
	}
}
