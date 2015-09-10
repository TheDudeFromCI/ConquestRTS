package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class DistantEntityHandler{
	private EntityGroup visibleEntities;
	private final WorldNoiseMachine machine;
	private final Dynmap dynmap;
	private final DynmapEntityBook book;
	public DistantEntityHandler(WorldNoiseMachine machine, Dynmap dynmap, BatchList batchList){
		this.machine = machine;
		this.dynmap = dynmap;
		book = batchList.getBook();
	}
	public boolean isFullyLoaded(){
		updateDynmap(dynmap.getX(), dynmap.getZ());
		return visibleEntities.isFullyLoaded();
	}
	public void update(){
		if(visibleEntities!=null&&!visibleEntities.isFullyLoaded())
			visibleEntities.loadStep();
	}
	private void updateDynmap(int x, int z){
		x += (32768-8192)/2;
		z += (32768-8192)/2;
		if(visibleEntities==null||visibleEntities.getX()!=x||visibleEntities.getZ()!=z){
			if(visibleEntities!=null)
				visibleEntities.dispose();
			visibleEntities = new EntityGroup(machine, book, x, z);
		}
	}
}
