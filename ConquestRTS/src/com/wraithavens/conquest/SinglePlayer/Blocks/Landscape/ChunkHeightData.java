package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;

public class ChunkHeightData{
	private static final int Size = Dynmap.BlocksPerChunk/LandscapeChunk.LandscapeSize;
	private final int[][] heights = new int[Size*Size][2];
	private int startX;
	private int startZ;
	public void load(int x, int z){
		startX = x;
		startZ = z;
	}
	private int[] getChunkHeight(int x, int z){
		// ---
		// TODO Make this actually get some important data.
		// ---
		// x = Algorithms.groupLocation(x, LandscapeChunk.LandscapeSize);
		// return heights[x];
		return new int[]{
			2944, 1
		};
	}
}
