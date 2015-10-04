package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.util.Arrays;

class IndexStorage{
	private short[] indices = new short[100];
	private int size;
	void clear(){
		size = 0;
	}
	short[] getAll(){
		return indices;
	}
	void place(short i){
		if(size==indices.length)
			indices = Arrays.copyOf(indices, indices.length+100);
		indices[size] = i;
		size++;
	}
	int size(){
		return size;
	}
}
