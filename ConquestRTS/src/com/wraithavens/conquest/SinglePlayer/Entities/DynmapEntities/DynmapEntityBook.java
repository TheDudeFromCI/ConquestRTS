package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.HashMap;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.Utility.BinaryFile;

class DynmapEntityBook{
	private final BatchList batchList;
	private final HashMap<EntityType,DynmapEntityBatch> batches = new HashMap();
	private final int x;
	private final int z;
	DynmapEntityBook(BatchList batchList, int x, int z){
		this.batchList = batchList;
		this.x = x;
		this.z = z;
	}
	void addEntity(EntityType type, GrassTransform transform, boolean update){
		if(batches.containsKey(type)){
			DynmapEntityBatch b = batches.get(type);
			b.addEntity(transform);
			if(update)
				b.rebuildBuffer();
		}else{
			MainLoop.endLoopTasks.add(new Runnable(){
				public void run(){
					DynmapEntityBatch b = new DynmapEntityBatch(type, x, z, 8192);
					b.addEntity(transform);
					batches.put(type, b);
					batchList.addBatch(b);
					if(update)
						b.rebuildBuffer();
				}
			});
			while(!batches.containsKey(type))
				try{
					Thread.sleep(1);
				}catch(Exception exception){
					exception.printStackTrace();
				}
		}
	}
	void dispose(){
		for(EntityType type : batches.keySet()){
			DynmapEntityBatch b = batches.get(type);
			b.dispose();
			batchList.removeBatch(b);
		}
	}
	int getTotalSize(){
		int i = 0;
		for(EntityType type : batches.keySet())
			i += batches.get(type).getRealCount();
		return i;
	}
	void rebuildBuffer(EntityType type){
		batches.get(type).rebuildBuffer();
	}
	void rebuildBuffers(){
		for(EntityType type : batches.keySet())
			batches.get(type).rebuildBuffer();
	}
	void save(BinaryFile bin){
		bin.addInt(getTotalSize());
		for(EntityType type : batches.keySet())
			batches.get(type).save(bin);
	}
}
