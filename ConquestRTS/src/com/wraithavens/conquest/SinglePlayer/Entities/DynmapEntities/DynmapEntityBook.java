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
	void addEntity(EntityType type, EntityTransform transform){
		synchronized(batches){
			if(batches.containsKey(type)){
				DynmapEntityBatch b = batches.get(type);
				b.addEntity(transform);
			}else{
				DynmapEntityBatch b = new DynmapEntityBatch(type);
				b.addEntity(transform);
				batches.put(type, b);
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
	boolean hasDistantlyVisible(){
		synchronized(batches){
			for(EntityType type : batches.keySet())
				if(batches.get(type).hasDistantlyVisible())
					return true;
		}
		return false;
	}
	void rebuildBuffer(EntityType type){
		synchronized(batches){
			batches.get(type).rebuildBuffer();
		}
	}
	void rebuildBuffers(){
		synchronized(batches){
			for(EntityType type : batches.keySet())
				batches.get(type).rebuildBuffer();
		}
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
		rebuildBuffers();
	}
}
