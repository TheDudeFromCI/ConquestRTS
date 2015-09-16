package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;
import com.wraithavens.conquest.Utility.Algorithms;

public class DistantEntityHandler{
	private EntityGroup visibleEntities;
	private final WorldNoiseMachine machine;
	private final Camera camera;
	private final DynmapEntityBook book;
	public DistantEntityHandler(WorldNoiseMachine machine, Camera camera, BatchList batchList){
		this.machine = machine;
		this.camera = camera;
		book = batchList.getBook();
	}
	public boolean isFullyLoaded(){
		updateDynmap(Algorithms.groupLocation((int)camera.x, 2048),
			Algorithms.groupLocation((int)camera.z, 2048));
		return visibleEntities.isFullyLoaded();
	}
	public void update(){
		if(visibleEntities!=null&&!visibleEntities.isFullyLoaded())
			visibleEntities.loadStep();
	}
	private void updateDynmap(int x, int z){
		if(visibleEntities==null||visibleEntities.getX()!=x||visibleEntities.getZ()!=z){
			if(visibleEntities!=null)
				visibleEntities.dispose();
			visibleEntities = new EntityGroup(machine, book, x, z);
		}
	}
}
