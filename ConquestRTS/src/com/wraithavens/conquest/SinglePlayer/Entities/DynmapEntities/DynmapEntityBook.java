package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.HashMap;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkListener;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.LandscapeWorld;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Camera;

class DynmapEntityBook{
	private final BatchList batchList;
	private final HashMap<EntityType,DynmapEntityBatch> batches = new HashMap();
	private final Camera camera;
	private final LandscapeWorld landscape;
	DynmapEntityBook(BatchList batchList, Camera camera, LandscapeWorld landscape){
		this.batchList = batchList;
		this.camera = camera;
		this.landscape = landscape;
		landscape.setListener(new ChunkListener(){
			public void chunksChanged(){
				updateVisibility();
			}
		});
	}
	void addEntity(EntityTransform transform){
		synchronized(batches){
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
	void clear(){
		for(EntityType type : batches.keySet())
			batches.get(type).dispose();
		batches.clear();
	}
	void dispose(){
		synchronized(batches){
			for(EntityType type : batches.keySet()){
				DynmapEntityBatch b = batches.get(type);
				b.dispose();
				batchList.removeBatch(b);
			}
		}
	}
	int getTotalSize(){
		int i = 0;
		synchronized(batches){
			for(EntityType type : batches.keySet())
				i += batches.get(type).getRealCount();
		}
		return i;
	}
	boolean hasCloselyVisible(){
		synchronized(batches){
			for(EntityType type : batches.keySet())
				if(batches.get(type).hasCloslyVisible())
					return true;
		}
		return false;
	}
	void removeEntity(EntityType type, EntityTransform transform){
		synchronized(batches){
			batches.get(type).removeEntity(transform);
		}
	}
	void updateVisibility(){
		synchronized(batches){
			for(EntityType type : batches.keySet())
				batches.get(type).updateVisibility(camera, landscape);
		}
	}
}
