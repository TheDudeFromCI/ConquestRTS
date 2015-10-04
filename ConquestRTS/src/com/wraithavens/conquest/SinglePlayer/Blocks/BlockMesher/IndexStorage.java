package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.util.Arrays;

public class IndexStorage{
	private short[] indices = new short[100];
	private int size;
	public void clear(){
		size = 0;
	}
	public short get(int index){
		return indices[index];
	}
	public short[] getAll(){
		return indices;
	}
	public void place(short i){
		if(size==indices.length)
			indices = Arrays.copyOf(indices, indices.length+100);
		indices[size] = i;
		size++;
	}
	public int size(){
		return size;
	}
}
