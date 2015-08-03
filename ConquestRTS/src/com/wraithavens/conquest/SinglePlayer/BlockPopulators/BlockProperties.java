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
