package com.wraithavens.conquest.SinglePlayer.Blocks;

import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;

class RawChunk{
	private static final int getBlockIndex(int x, int y, int z){
		return (x&15)+(y&15)*16+(z&15)*256;
	}
	private final byte[] blocks = new byte[4096];
	private final int x;
	private final int y;
	private final int z;
	private int blockCount = 0;
	RawChunk(int x, int y, int z){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = Block.AIR;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public boolean isEmpty(){
		return blockCount==0;
	}
	public void setBlock(int x, int y, int z, byte type){
		int index = getBlockIndex(x, y, z);
		if(blocks[index]!=type){
			if(blocks[index]==Block.AIR)
				blockCount++;
			else if(type==Block.AIR)
				blockCount--;
			blocks[index] = type;
		}
	}
	byte getBlock(int x, int y, int z){
		return blocks[getBlockIndex(x, y, z)];
	}
}
