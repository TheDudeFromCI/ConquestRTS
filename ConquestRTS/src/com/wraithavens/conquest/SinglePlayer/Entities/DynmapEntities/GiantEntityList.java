package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkWorkerQue;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.MassChunkHeightData;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class GiantEntityList{
	private static void addEntityToChunk(GiantEntityListEntry entity, File file){
		BinaryFile bin = new BinaryFile(file);
		bin.decompress(false);
		BinaryFile bin2 = new BinaryFile(bin.size()+5*4);
		int i, a;
		{
			// ---
			// Copy vertex data.
			// ---
			int vertexCount = bin.getInt();
			int indexCount = bin.getInt();
			bin2.addInt(vertexCount);
			bin2.addInt(indexCount);
			for(i = 0; i<vertexCount; i++){
				bin2.addFloat(bin.getFloat());
				bin2.addFloat(bin.getFloat());
				bin2.addFloat(bin.getFloat());
				bin2.addByte(bin.getByte());
			}
			for(i = 0; i<indexCount; i++)
				bin2.addInt(bin.getInt());
		}
		{
			// ---
			// Copy plant data.
			// ---
			HashMap<EntityType,ArrayList<float[]>> entityData = new HashMap();
			{
				int plantTypes = bin.getInt();
				EntityType tempType;
				int locationCount;
				ArrayList<float[]> list;
				for(i = 0; i<plantTypes; i++){
					tempType = EntityType.values()[bin.getInt()];
					locationCount = bin.getInt();
					if(entityData.containsKey(tempType))
						list = entityData.get(tempType);
					else{
						list = new ArrayList();
						entityData.put(tempType, list);
					}
					for(a = 0; a<locationCount; a++)
						list.add(new float[]{
							bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()
						});
				}
				if(entityData.containsKey(entity.getType())){
					list = entityData.get(entity.getType());
					bin2.allocateBytes(8);
				}else{
					list = new ArrayList();
					entityData.put(entity.getType(), list);
				}
				list.add(new float[]{
					entity.getX(), entity.getY(), entity.getZ(), entity.getR(), entity.getS(),
				});
			}
			{
				bin2.addInt(entityData.size());
				for(EntityType tempType : entityData.keySet()){
					bin2.addInt(tempType.ordinal());
					bin2.addInt(entityData.get(tempType).size());
					for(float[] f : entityData.get(tempType))
						for(float f2 : f)
							bin2.addFloat(f2);
				}
			}
		}
		{
			// ---
			// Copy rest of data.
			// ---
			while(bin.getRemaining()>0)
				bin2.addByte(bin.getByte());
		}
		bin2.compress(false);
		bin2.compile(file);
	}
	private final ArrayList<GiantEntityListEntry> list = new ArrayList();
	private final ArrayList<GiantEntityListEntry> tempList = new ArrayList();
	private final ChunkWorkerQue que;
	private final WorldNoiseMachine machine;
	private int x;
	private int z;
	GiantEntityList(ChunkWorkerQue que, WorldNoiseMachine machine){
		this.que = que;
		this.machine = machine;
	}
	private void save(){
		File file = Algorithms.getDistantEntityListPath(x, z);
		BinaryFile bin = new BinaryFile(list.size()*22);
		for(GiantEntityListEntry e : list){
			bin.addShort((short)e.getType().ordinal());
			bin.addFloat(e.getX());
			bin.addFloat(e.getY());
			bin.addFloat(e.getZ());
			bin.addFloat(e.getR());
			bin.addFloat(e.getS());
		}
		bin.compress(false);
		bin.compile(file);
	}
	void addEntity(float x, float y, float z, float r, float s, EntityType type, boolean save){
		GiantEntityListEntry entity = new GiantEntityListEntry(type, x, y, z, r, s);
		list.add(entity);
		if(save)
			save();
		int blockX = Algorithms.groupLocation((int)x, 64);
		int blockY = Algorithms.groupLocation((int)y, 64);
		int blockZ = Algorithms.groupLocation((int)z, 64);
		File file = Algorithms.getChunkPath(blockX, blockY, blockZ);
		if(file.exists()&&file.length()>0)
			addEntityToChunk(entity, file);
		else{
			tempList.add(entity);
			que.addTask(blockX, blockY, blockZ, new ChunkHeightData(machine, blockX, blockZ,
				new MassChunkHeightData(blockX, blockZ)));
		}
	}
	void getEntitiesInChunk(int x, int y, int z, ArrayList<GiantEntityListEntry> out){
		int a, b, c;
		for(GiantEntityListEntry e : list){
			a = Algorithms.groupLocation((int)e.getX(), 64);
			b = Algorithms.groupLocation((int)e.getY(), 64);
			c = Algorithms.groupLocation((int)e.getZ(), 64);
			if(x==a&&y==b&&z==c)
				out.add(e);
		}
	}
	void load(int x, int z){
		this.x = x;
		this.z = z;
		list.clear();
		File file = Algorithms.getDistantEntityListPath(x, z);
		BinaryFile bin = new BinaryFile(file);
		while(bin.getRemaining()>0)
			list.add(new GiantEntityListEntry(EntityType.values()[bin.getShort()], bin.getFloat(), bin
				.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()));
	}
	void update(){
		if(tempList.isEmpty())
			return;
		File file;
		GiantEntityListEntry e;
		for(int i = 0; i<tempList.size(); i++){
			e = tempList.get(i);
			file =
				Algorithms.getChunkPath(Algorithms.groupLocation((int)e.getY(), 64),
					Algorithms.groupLocation((int)e.getX(), 64), Algorithms.groupLocation((int)e.getZ(), 64));
			if(file.exists()&&file.length()>0){
				tempList.remove(i);
				addEntityToChunk(e, file);
				return;
			}
			try{
				Thread.sleep(1);
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
}
