package com.wraithavens.conquest.SinglePlayer.Entities.Grass;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;

public class GrassPatch{
	private final ArrayList<GrassTransform> grassBlades;
	private final EntityType grassType;
	private final int x;
	private final int z;
	public GrassPatch(EntityType grassType, ArrayList<GrassTransform> grassBlades, int x, int z){
		this.grassType = grassType;
		this.grassBlades = grassBlades;
		this.x = x;
		this.z = z;
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
	boolean inView(LandscapeWorld landscape){
		return landscape.isWithinView(x, z);
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
