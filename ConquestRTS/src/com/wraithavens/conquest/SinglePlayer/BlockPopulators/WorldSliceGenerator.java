package com.wraithavens.conquest.SinglePlayer.BlockPopulators;


public class WorldSliceGenerator{
	private final int[][] heights = new int[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
	private final WorldGenerator worldGen;
	public WorldSliceGenerator(WorldGenerator worldGen){
		this.worldGen = worldGen;
	}
}