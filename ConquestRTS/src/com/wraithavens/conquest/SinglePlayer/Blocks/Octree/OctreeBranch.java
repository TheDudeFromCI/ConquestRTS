package com.wraithavens.conquest.SinglePlayer.Blocks.Octree;

import com.wraithavens.conquest.SinglePlayer.Blocks.VoxelChunk;

class OctreeBranch{
	OctreeBranch[] children;
	final VoxelChunk owner;
	OctreeBranch(VoxelChunk owner){
		this.owner = owner;
	}
}
