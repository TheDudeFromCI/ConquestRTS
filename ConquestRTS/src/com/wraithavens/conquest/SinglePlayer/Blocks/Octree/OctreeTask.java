package com.wraithavens.conquest.SinglePlayer.Blocks.Octree;

import com.wraithavens.conquest.SinglePlayer.Blocks.VoxelChunk;

public abstract class OctreeTask{
	private final Octree octree;
	public OctreeTask(Octree octree){
		this.octree = octree;
	}
	public abstract void run(VoxelChunk chunk);
	public final void runTask(){
		octree.runTask(this);
	}
	public abstract boolean shouldRun(VoxelChunk voxel);
}
