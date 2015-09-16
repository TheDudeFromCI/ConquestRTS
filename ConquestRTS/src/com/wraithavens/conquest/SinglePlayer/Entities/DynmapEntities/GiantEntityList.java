package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
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
	private int x;
	private int z;
	void addEntity(float x, float y, float z, float r, float s, EntityType type){
		GiantEntityListEntry entity = new GiantEntityListEntry(type, x, y, z, r, s);
		list.add(entity);
		int blockX = Algorithms.groupLocation((int)x, 64);
		int blockY = Algorithms.groupLocation((int)y, 64);
		int blockZ = Algorithms.groupLocation((int)z, 64);
		if(blockX-this.x>=(8192-2048)/2&&blockZ-this.z>=(8192-2048)/2&&blockX-this.x<(8192-2048)/2+2048
			&&blockZ-this.z<(8192-2048)/2+2048){
			File file = Algorithms.getChunkPath(blockX, blockY, blockZ);
			if(file.exists()&&file.length()>0)
				addEntityToChunk(entity, file);
			else
				tempList.add(entity);
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
	ArrayList<GiantEntityListEntry> getList(){
		return list;
	}
	boolean isEmpty(){
		return list.isEmpty();
	}
	void load(int x, int z){
		this.x = x;
		this.z = z;
		list.clear();
		File file = Algorithms.getDistantEntityListPath(x, z);
		if(!(file.exists()&&file.length()>0))
			return;
		BinaryFile bin = new BinaryFile(file);
		int size = bin.getInt();
		for(int i = 0; i<size; i++)
			list.add(new GiantEntityListEntry(EntityType.values()[bin.getShort()], bin.getFloat(), bin
				.getFloat(), bin.getFloat(), bin.getFloat(), bin.getFloat()));
		size = bin.getInt();
		for(int i = 0; i<size; i++)
			tempList.add(list.get(bin.getInt()));
	}
	boolean needsUpdate(){
		return tempList.size()>0;
	}
	void save(){
		File file = Algorithms.getDistantEntityListPath(x, z);
		BinaryFile bin = new BinaryFile(list.size()*22+8+tempList.size()*4);
		bin.addInt(list.size());
		for(GiantEntityListEntry e : list){
			bin.addShort((short)e.getType().ordinal());
			bin.addFloat(e.getX());
			bin.addFloat(e.getY());
			bin.addFloat(e.getZ());
			bin.addFloat(e.getR());
			bin.addFloat(e.getS());
		}
		bin.addInt(tempList.size());
		for(GiantEntityListEntry e : tempList)
			bin.addInt(list.indexOf(e));
		bin.compress(false);
		bin.compile(file);
	}
	int size(){
		return list.size();
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
