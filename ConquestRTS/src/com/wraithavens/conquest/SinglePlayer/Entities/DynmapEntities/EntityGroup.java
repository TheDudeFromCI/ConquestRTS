package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityDatabase;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityGroup{
	private final int x;
	private final int z;
	private final ArrayList<DynmapEntity> giantEntities = new ArrayList();
	private final EntityDatabase database;
	private EntityGroupLoadProtocol loadProtocol;
	public EntityGroup(WorldNoiseMachine machine, EntityDatabase database, int x, int z){
		System.out.println("Loaded entity group: ["+x+", "+z+"]");
		this.database = database;
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
						DynmapEntity e;
						for(int i = 0; i<entityCount; i++){
							e = new DynmapEntity(EntityType.values()[bin.getInt()]);
							e.moveTo(bin.getFloat(), bin.getFloat(), bin.getFloat());
							e.scaleTo(bin.getFloat());
							e.setYaw(bin.getFloat());
							addEntity(e);
						}
						if(!bin.getBoolean())
							loadProtocol =
								new EntityGroupLoadProtocol(machine, EntityGroup.this, x, z, file, bin);
					}
				});
			}else
				loadProtocol = new EntityGroupLoadProtocol(machine, this, x, z, file, null);
		}
	}
	void addEntity(DynmapEntity e){
		synchronized(giantEntities){
			giantEntities.add(e);
		}
		database.addEntity(e);
	}
	void dispose(){
		if(loadProtocol!=null){
			loadProtocol.dispose();
			loadProtocol = null;
		}
		MainLoop.endLoopTasks.add(new Runnable(){
			public void run(){
				synchronized(giantEntities){
					for(DynmapEntity e : giantEntities)
						database.removeEntity(e);
				}
			}
		});
		synchronized(giantEntities){
			giantEntities.clear();
		}
	}
	int getEntityCount(){
		synchronized(giantEntities){
			return giantEntities.size();
		}
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
		bin.addInt(giantEntities.size());
		synchronized(giantEntities){
			for(DynmapEntity e : giantEntities){
				bin.addInt(e.getType().ordinal());
				bin.addFloat(e.getX());
				bin.addFloat(e.getY());
				bin.addFloat(e.getZ());
				bin.addFloat(e.getScale());
				bin.addFloat(e.getYaw());
			}
		}
	}
}
