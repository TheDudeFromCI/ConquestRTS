package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import com.wraithavens.conquest.SinglePlayer.Heightmap.Dynmap;

public class ChunkHeightData{
	private final int[][] heights = new int[(Dynmap.BlocksPerChunk<<1)/LandscapeChunk.LandscapeSize][2];
	private int[] getChunkHeight(int x, int z){
		x = (x>>)&heights.length-1;
		return heights[x];
	}
}
