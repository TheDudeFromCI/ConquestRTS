package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.HashMap;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;

public class GiantEntityDictionary{
	private final HashMap<Biome,DictionaryEntry> biomes = new HashMap();
	private final int spawnRate;
	private final float minDistance;
	GiantEntityDictionary(){
		HashMap<Biome,TempDictionaryEntry> averageDistances = new HashMap();
		averageDistances.put(Biome.ArcstoneHills, new TempDictionaryEntry(400.0, EntityType.Arcstone1, 7));
		double a = rebuild(averageDistances);
		spawnRate = (int)(8192/a*(8192/a));
		minDistance = (float)(a/2);
	}
	private double rebuild(HashMap<Biome,TempDictionaryEntry> averageDistances){
		double minDistance = Integer.MAX_VALUE;
		double d;
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
	float getMinDistance(){
		return minDistance;
	}
	int getSpawnRate(){
		return spawnRate;
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
