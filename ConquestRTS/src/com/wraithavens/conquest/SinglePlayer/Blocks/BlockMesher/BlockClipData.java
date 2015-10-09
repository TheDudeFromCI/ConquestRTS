package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

class BlockClipData{
	private static int getIndex(int x, int y, int z){
		{
			// Test to make sure point is within bounds.
			int fails = 0;
			if(x<0)
				fails++;
			if(x>=64)
				fails++;
			if(y<0)
				fails++;
			if(y>=64)
				fails++;
			if(z<0)
				fails++;
			if(z>=64)
				fails++;
			if(fails!=1)
				throw new RuntimeException();
		}
		if(x<0)
			return 64*64*0+y*64+z;
		if(x>=64)
			return 64*64*1+y*64+z;
		if(y<0)
			return 64*64*2+x*64+z;
		if(y>=64)
			return 64*64*3+x*64+z;
		if(z<0)
			return 64*64*4+x*64+y;
		if(z>=64)
			return 64*64*5+x*64+y;
		return -1;
	}
	private final byte[] blocks = new byte[64*64*6];
	void clear(){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = BlockData.Air;
	}
	byte[] getBytes(){
		return blocks;
	}
	byte hasBlock(int x, int y, int z){
		return blocks[getIndex(x, y, z)];
	}
	void setHasBlockWeak(int x, int y, int z, byte block){
		try{
			blocks[getIndex(x, y, z)] = block;
		}catch(Exception exception){}
	}
}
