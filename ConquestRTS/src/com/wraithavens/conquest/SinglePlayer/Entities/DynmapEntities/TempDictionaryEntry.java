package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class TempDictionaryEntry{
	private final double averageDistance;
	private final EntityType mainEntity;
	private final int entityVariations;
	TempDictionaryEntry(double averageDistance, EntityType mainEntity, int entityVariations){
		this.averageDistance = averageDistance;
		this.mainEntity = mainEntity;
		this.entityVariations = entityVariations;
	}
	double getAverageDistance(){
		return averageDistance;
	}
	int getEntityVariations(){
		return entityVariations;
	}
	EntityType getMainEntity(){
		return mainEntity;
	}
}
