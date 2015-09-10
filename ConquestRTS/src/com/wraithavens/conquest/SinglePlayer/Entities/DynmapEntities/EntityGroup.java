package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.wraithavens.conquest.Launcher.MainLoop;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class EntityGroup{
	private final int x;
	private final int z;
	private final DynmapEntityBook book;
	private final HashMap<EntityType,ArrayList<EntityTransform>> entities = new HashMap();
	private EntityGroupLoadProtocol loadProtocol;
	public EntityGroup(WorldNoiseMachine machine, DynmapEntityBook book, int x, int z){
		System.out.println("Loaded entity group: ["+x+", "+z+"]");
		this.book = book;
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
							addEntity(
								EntityType.values()[bin.getInt()],
								new EntityTransform(bin.getFloat(), bin.getFloat(), bin.getFloat(), bin
									.getFloat(), bin.getFloat(), bin.getInt()), false);
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
	void addEntity(EntityType type, EntityTransform e, boolean update){
		book.addEntity(type, e, update);
		if(entities.containsKey(type))
			entities.get(type).add(e);
		else{
			ArrayList<EntityTransform> list = new ArrayList();
			list.add(e);
			entities.put(type, list);
		}
	}
	void dispose(){
		if(loadProtocol!=null){
			loadProtocol.dispose();
			loadProtocol = null;
		}
		for(EntityType type : entities.keySet())
			for(EntityTransform t : entities.get(type)){
				book.removeEntity(type, t);
			}
		book.rebuildBuffers();
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
		int i = 0;
		for(EntityType type : entities.keySet())
			i += entities.get(type).size();
		bin.addInt(i);
		for(EntityType type : entities.keySet())
			for(EntityTransform t : entities.get(type)){
				bin.addInt(type.ordinal());
				bin.addFloat(t.getX());
				bin.addFloat(t.getY());
				bin.addFloat(t.getZ());
				bin.addFloat(t.getRotation());
				bin.addFloat(t.getScale());
				bin.addInt(t.getVisibilityLevel());
			}
	}
}
