package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public class BlockProperties{
	private final byte[] blocks;
	private int size;
	public BlockProperties(int maxSize){
		blocks = new byte[maxSize];
		size = 0;
	}
	public boolean contains(byte block){
		for(int i = 0; i<size; i++)
			if(blocks[i]==block)
				return true;
		return false;
	}
	public byte get(int index){
		return blocks[index];
	}
	public byte getMostOccuring(){
		if(size==0)
			return Block.AIR;
		int count = 1, tempCount;
		byte popular = blocks[0];
		byte temp = 0;
		for(int i = 0; i<size-1; i++){
			temp = blocks[i];
			tempCount = 0;
			for(int j = 1; j<size; j++)
				if(temp==blocks[j])
					tempCount++;
			if(tempCount>count){
				popular = temp;
				count = tempCount;
			}
		}
		return popular;
	}
	public void place(byte block){
		blocks[size] = block;
		size++;
	}
	public void reset(){
		size = 0;
	}
	public int size(){
		return size;
	}
}
