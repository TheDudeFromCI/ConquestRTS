package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.ArrayList;
import java.util.HashMap;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

class DynmapEntityBook{
	private final BatchList batchList;
	private final HashMap<EntityType,DynmapEntityBatch> batches = new HashMap();
	private final Camera camera;
	private final LandscapeWorld landscape;
	private int totalSize;
	DynmapEntityBook(BatchList batchList, Camera camera, LandscapeWorld landscape){
		this.batchList = batchList;
		this.camera = camera;
		this.landscape = landscape;
	}
	void dispose(){
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				for(EntityType type : batches.keySet()){
					DynmapEntityBatch b = batches.get(type);
					b.dispose();
				}
				batches.clear();
				batchList.clearBatches();
			}
		});
	}
	int getTotalSize(){
		return totalSize;
	}
	boolean hasCloselyVisible(){
		for(EntityType type : batches.keySet())
			if(batches.get(type).hasCloslyVisible())
				return true;
		return false;
	}
	void setEntities(ArrayList<EntityTransform> transforms){
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				totalSize = transforms.size();
				for(EntityTransform transform : transforms){
					if(batches.containsKey(transform.getType())){
						DynmapEntityBatch b = batches.get(transform.getType());
						b.addEntity(transform);
					}else{
						DynmapEntityBatch b = new DynmapEntityBatch(transform.getType());
						b.addEntity(transform);
						batches.put(transform.getType(), b);
						batchList.addBatch(b);
					}
				}
			}
		});
	}
	void updateVisibility(){
		for(EntityType type : batches.keySet())
			batches.get(type).updateVisibility(camera, landscape);
	}
}
