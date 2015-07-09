package com.wraithavens.conquest.SinglePlayer.BlockPopulators;


class BlockProperties{
	private final byte[] blocks;
	private int size;
	BlockProperties(int maxSize){
		blocks = new byte[maxSize];
		size = 0;
	}
	void reset(){
		size = 0;
	}
	void place(byte block){
		blocks[size] = block;
		size++;
	}
	boolean contains(byte block){
		for(int i = 0; i<size; i++)
			if(blocks[i]==block)return true;
		return false;
	}
	int size(){
		return size;
	}
	byte get(int index){
		return blocks[index];
	}
}