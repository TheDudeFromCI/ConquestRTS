package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import com.wraithavens.conquest.SinglePlayer.Entities.AABB;

public class BatchQuadTree{
	private final AABB aabb;
	private final BatchQuadTree[] children = new BatchQuadTree[4];
	private final int x;
	private final int z;
	private final int size;
	BatchQuadTree(int x, int z, int size){
		aabb = new AABB();
		aabb.set(x, -10000, z, x+size, 10000, z+size);
		this.x = x;
		this.z = z;
		this.size = size;
	}
	BatchQuadTree createChild(int index){
		int s = size/2;
		children[index] = new BatchQuadTree((index&1)==1?x+s:x, (index&2)==2?z+s:z, s);
		return children[index];
	}
	AABB getAABB(){
		return aabb;
	}
	BatchQuadTree getChild(int index){
		return children[index];
	}
}
