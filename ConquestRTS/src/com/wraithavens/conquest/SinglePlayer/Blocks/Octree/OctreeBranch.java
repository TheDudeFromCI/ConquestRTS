package com.wraithavens.conquest.SinglePlayer.Blocks.Octree;

import com.wraithavens.conquest.SinglePlayer.Blocks.VoxelChunk;

public class OctreeBranch{
	OctreeBranch[] children;
	final VoxelChunk owner;
	OctreeBranch(VoxelChunk owner){
		this.owner = owner;
	}
}
