package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

public class BlockClipData{
	private static int getIndex(int x, int y, int z){
		{
			// Test to make sure point is within bounds.
			int fails = 0;
			if(x<0)
				fails++;
			if(x>=32)
				fails++;
			if(y<0)
				fails++;
			if(y>=32)
				fails++;
			if(z<0)
				fails++;
			if(z>=32)
				fails++;
			if(fails!=1)
				throw new RuntimeException();
		}
		if(x<0)
			return 32*32*0+y*32+z;
		if(x>=32)
			return 32*32*1+y*32+z;
		if(y<0)
			return 32*32*2+x*32+z;
		if(y>=32)
			return 32*32*3+x*32+z;
		if(z<0)
			return 32*32*4+x*32+y;
		if(z>=32)
			return 32*32*5+x*32+y;
		return -1;
	}
	private final byte[] blocks = new byte[32*32*6];
	public void clear(){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = BlockData.Air;
	}
	public byte[] getBytes(){
		return blocks;
	}
	public byte hasBlock(int x, int y, int z){
		return blocks[getIndex(x, y, z)];
	}
	public void setHasBlockWeak(int x, int y, int z, byte block){
		try{
			blocks[getIndex(x, y, z)] = block;
		}catch(Exception exception){}
	}
}
