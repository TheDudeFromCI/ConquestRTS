package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.HashMap;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;

public class GiantEntityDictionary{
	private final HashMap<Biome,DictionaryEntry> biomes = new HashMap();
	private final float averageDistanceApart;
	GiantEntityDictionary(){
		HashMap<Biome,TempDictionaryEntry> averageDistances = new HashMap();
		averageDistances.put(Biome.ArcstoneHills, new TempDictionaryEntry(200f, EntityType.Arcstone1, 1));
		averageDistanceApart = rebuild(averageDistances)*2;
	}
	private float rebuild(HashMap<Biome,TempDictionaryEntry> averageDistances){
		float minDistance = Float.MAX_VALUE;
		float d;
		for(Biome biome : averageDistances.keySet()){
			d = averageDistances.get(biome).getAverageDistance();
			if(d<minDistance)
				minDistance = d;
		}
		TempDictionaryEntry entry;
		for(Biome biome : averageDistances.keySet()){
			entry = averageDistances.get(biome);
			biomes.put(biome, new DictionaryEntry(entry.getAverageDistance()/minDistance, entry.getMainEntity(),
				entry.getEntityVariations()));
		}
		return minDistance;
	}
	float getAverageDistance(){
		return averageDistanceApart;
	}
	float getMinDistance(){
		return averageDistanceApart/2;
	}
	EntityType randomEntity(Biome biome){
		DictionaryEntry e = biomes.get(biome);
		if(e==null)
			return null;
		if(Math.random()>e.getSpawnChance())
			return null;
		return EntityType.getVariation(e.getEntity(), (int)(Math.random()*e.getVariations()));
	}
}
