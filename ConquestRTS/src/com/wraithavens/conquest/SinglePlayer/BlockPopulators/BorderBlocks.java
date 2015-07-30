package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public class BorderBlocks{
	private final boolean[][] blocks;
	public BorderBlocks(){
		blocks = new boolean[6][Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK];
	}
	public void setBlock(int x, int y, int z, int j, boolean block){
		if(j==0||j==1)blocks[j][y+z*Chunk.BLOCKS_PER_CHUNK] = block;
		else if(j==2||j==3)blocks[j][x+z*Chunk.BLOCKS_PER_CHUNK] = block;
		else blocks[j][x+y*Chunk.BLOCKS_PER_CHUNK] = block;
	}
	public boolean getBlock(int x, int y, int z, int j){
		if(j==0||j==1)return blocks[j][y+z*Chunk.BLOCKS_PER_CHUNK];
		if(j==2||j==3)return blocks[j][x+z*Chunk.BLOCKS_PER_CHUNK];
		return blocks[j][x+y*Chunk.BLOCKS_PER_CHUNK];
	}
}