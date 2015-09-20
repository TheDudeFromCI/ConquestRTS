package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.util.Arrays;

class IndexStorage{
	private int[] indices = new int[100];
	private int size;
	public int get(int index){
		return indices[index];
	}
	public void place(int i){
		if(size==indices.length)
			indices = Arrays.copyOf(indices, indices.length+100);
		indices[size] = i;
		size++;
	}
	public int size(){
		return size;
	}
	void clear(){
		size = 0;
	}
}
