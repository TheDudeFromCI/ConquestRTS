package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Entities.Grass.GrassTransform;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityGroup{
	private final int x;
	private final int z;
	private final DynmapEntityBook book;
	private EntityGroupLoadProtocol loadProtocol;
	public EntityGroup(WorldNoiseMachine machine, BatchList batchList, int x, int z){
		System.out.println("Loaded entity group: ["+x+", "+z+"]");
		book = new DynmapEntityBook(batchList);
		this.x = x;
		this.z = z;
		{
			File file = Algorithms.getDistantEntityGroupPath(x, z);
			if(file.exists()&&file.length()>0){
				BinaryFile bin = new BinaryFile(file);
				bin.decompress(true);
				int entityCount = bin.getInt();
				MainLoop.endLoopTasks.add(new Runnable(){
					public void run(){
						for(int i = 0; i<entityCount; i++)
							book.addEntity(EntityType.values()[bin.getInt()], new GrassTransform(bin.getFloat(),
								bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()), false);
						book.rebuildBuffers();
						if(!bin.getBoolean())
							loadProtocol =
							new EntityGroupLoadProtocol(machine, EntityGroup.this, x, z, file, bin);
					}
				});
			}else
				loadProtocol = new EntityGroupLoadProtocol(machine, this, x, z, file, null);
		}
	}
	void addEntity(EntityType type, GrassTransform e, boolean update){
		book.addEntity(type, e, update);
	}
	void dispose(){
		if(loadProtocol!=null){
			loadProtocol.dispose();
			loadProtocol = null;
		}
		book.dispose();
	}
	int getEntityCount(){
		return book.getTotalSize();
	}
	int getX(){
		return x;
	}
	int getZ(){
		return z;
	}
	boolean isFullyLoaded(){
		return loadProtocol==null;
	}
	void loadStep(){
		if(loadProtocol.update())
			loadProtocol = null;
	}
	void saveEntities(BinaryFile bin){
		book.save(bin);
	}
}
