package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.ChunkWorkerQue;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class GiantEntityList{
	private final ArrayList<GiantEntityListEntry> list = new ArrayList();
	private final ChunkWorkerQue que;
	private int x;
	private int z;
	GiantEntityList(ChunkWorkerQue que){
		this.que = que;
	}
	private void save(){
		File file = Algorithms.getDistantEntityListPath(x, z);
		BinaryFile bin = new BinaryFile(list.size()*4*4+4);
		bin.addInt(list.size());
		for(GiantEntityListEntry e : list){
			bin.addInt(e.getX());
			bin.addInt(e.getY());
			bin.addInt(e.getZ());
			bin.addInt(e.getType().ordinal());
		}
		bin.compress(false);
		bin.compile(file);
	}
	void addEntity(int x, int y, int z, EntityType type, boolean save){
		list.add(new GiantEntityListEntry(x, y, z, type));
		if(save)
			save();
		File file = Algorithms.getChunkPath(x, y, z);
		if(file.exists()&&file.length()>0){
			// TODO
		}
	}
	void getEntitiesInChunk(int x, int y, int z, ArrayList<GiantEntityListEntry> out){
		int a, b, c;
		for(GiantEntityListEntry e : list){
			a = Algorithms.groupLocation(e.getX(), 64);
			b = Algorithms.groupLocation(e.getY(), 64);
			c = Algorithms.groupLocation(e.getZ(), 64);
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
		int entities = bin.getInt();
		for(int i = 0; i<entities; i++)
			list.add(new GiantEntityListEntry(bin.getInt(), bin.getInt(), bin.getInt(), EntityType.values()[bin
				.getInt()]));
	}
}
