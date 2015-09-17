package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.PointGenerator2D;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class EntityGroup{
	private final int x;
	private final int z;
	private final DynmapEntityBook book;
	private final GiantEntityList list;
	public EntityGroup(WorldNoiseMachine machine, DynmapEntityBook book, int x, int z){
		System.out.println("Loaded entity group: ["+x+", "+z+"]");
		this.book = book;
		this.x = x;
		this.z = z;
		list = new GiantEntityList();
		{
			list.load(x, z);
			if(list.isEmpty()){
				GiantEntityDictionary dictionary = new GiantEntityDictionary();
				PointGenerator2D pointGen =
					new PointGenerator2D(machine.getGiantEntitySeed(), dictionary.getAverageDistance(),
						dictionary.getMinDistance(), 1.0f);
				ArrayList<float[]> locs = new ArrayList();
				pointGen.noise(x-(8192-2048)/2, z-(8192-2048)/2, 8192, locs);
				EntityType type;
				float[] htl = new float[3];
				for(float[] loc : locs){
					type =
						dictionary.randomEntity(machine.getBiomeAt((int)loc[0], (int)loc[1], htl),
							machine.getGiantEntitySeed()+1, (int)loc[0], (int)loc[1]);
					if(type==null)
						continue;
					list.addEntity(loc[0], machine.scaleHeight(htl[0], htl[1], htl[2], loc[0], loc[1]), loc[1],
						loc[2], loc[3], type);
				}
				list.save();
				System.out.println("  Generated "+list.size()+" giant entities.");
			}
			for(EntityTransform e : list.getList())
				book.addEntity(e.getType(),
					new EntityTransform(e.getType(), e.getX(), e.getY(), e.getZ(), e.getR(), e.getS(), 0));
			book.rebuildBuffers(false);
		}
	}
	void dispose(){
		book.clear();
	}
	int getEntityCount(){
		return book.getTotalSize();
	}
	GiantEntityList getList(){
		return list;
	}
	int getX(){
		return x;
	}
	int getZ(){
		return z;
	}
	boolean isFullyLoaded(){
		return !list.needsUpdate();
	}
	void loadStep(){
		list.update();
	}
}
