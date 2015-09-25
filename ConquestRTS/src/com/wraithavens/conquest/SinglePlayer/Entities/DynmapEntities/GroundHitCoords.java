package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.util.Arrays;

class GroundHitCoords{
	private int[] locations = new int[10];
	private int size;
	void clear(){
		size = 0;
	}
	int getSize(){
		return size/2;
	}
	int getX(int index){
		return locations[index*2];
	}
	int getZ(int index){
		return locations[index*2+1];
	}
	void place(int x, int z){
		for(int i = 0; i<size; i += 2)
			if(locations[i]==x&&locations[i+1]==z)
				return;
		if(size==locations.length)
			locations = Arrays.copyOf(locations, locations.length+30);
		locations[size] = x;
		locations[size+1] = z;
		size += 2;
	}
}
