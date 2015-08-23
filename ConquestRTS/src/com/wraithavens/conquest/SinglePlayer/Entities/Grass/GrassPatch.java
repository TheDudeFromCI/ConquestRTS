package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class GrassPatch{
	private final ArrayList<GrassTransform> grassBlades;
	private final EntityType grassType;
	public GrassPatch(EntityType grassType, ArrayList<GrassTransform> grassBlades){
		this.grassType = grassType;
		this.grassBlades = grassBlades;
	}
	int getCount(){
		return grassBlades.size();
	}
	ArrayList<GrassTransform> getGrassBlades(){
		return grassBlades;
	}
	EntityType getType(){
		return grassType;
	}
	void store(FloatBuffer data){
		for(GrassTransform t : grassBlades){
			data.put(t.getX());
			data.put(t.getY());
			data.put(t.getZ());
			data.put(t.getRotation());
			data.put(t.getScale());
		}
	}
}
