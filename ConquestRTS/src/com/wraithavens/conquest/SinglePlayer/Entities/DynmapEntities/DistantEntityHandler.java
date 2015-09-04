package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class DistantEntityHandler{
	private final EntityGroup[] visibleEntities = new EntityGroup[16];
	private final EntityDatabase database;
	private final WorldNoiseMachine machine;
	private final Dynmap dynmap;
	private boolean fullyLoaded = false;
	private int dynmapX = Integer.MAX_VALUE;
	private int dynmapZ = Integer.MAX_VALUE;
	public DistantEntityHandler(EntityDatabase database, WorldNoiseMachine machine, Dynmap dynmap){
		this.database = database;
		this.machine = machine;
		this.dynmap = dynmap;
	}
	public boolean isFullyLoaded(){
		updateDynmap();
		return fullyLoaded;
	}
	public void update(){
		if(fullyLoaded)
			return;
		for(int i = 0; i<16; i++)
			if(visibleEntities[i]!=null&&!visibleEntities[i].isFullyLoaded()){
				visibleEntities[i].loadStep();
				return;
			}
		fullyLoaded = true;
	}
	private void updateDynmap(){
		if(dynmapX!=dynmap.getX()||dynmapZ!=dynmap.getZ()){
			dynmapX = dynmap.getX();
			dynmapZ = dynmap.getZ();
			updateDynmap(dynmapX, dynmapZ);
		}
	}
	private void updateDynmap(int x, int z){
		System.out.println("Updated dynmap entities to dynmap: ["+x+", "+z+"]");
		int endX = x+32768;
		int endZ = z+32768;
		int a, b, i;
		for(i = 0; i<16; i++){
			if(visibleEntities[i]==null)
				continue;
			if(visibleEntities[i].getX()<x||visibleEntities[i].getZ()>=endX||visibleEntities[i].getZ()<z
				||visibleEntities[i].getZ()>=endZ){
				visibleEntities[i].dispose();
				visibleEntities[i] = null;
			}
		}
		for(a = x; a<endX; a += 8192)
			groupFinder:for(b = z; b<endZ; b += 8192){
				for(i = 0; i<16; i++)
					if(visibleEntities[i]!=null&&visibleEntities[i].getX()==a&&visibleEntities[i].getZ()==b)
						continue groupFinder;
				for(i = 0; i<16; i++)
					if(visibleEntities[i]==null){
						visibleEntities[i] = new EntityGroup(machine, database, a, b);
						fullyLoaded = false;
						continue groupFinder;
					}
				throw new IllegalStateException();
			}
	}
}
