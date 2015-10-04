package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.util.Arrays;

class BlockTypeList{
	private byte[] types = new byte[10];
	private int size;
	void add(byte t){
		for(int i = 0; i<size; i++)
			if(types[i]==t)
				return;
		if(size==types.length)
			types = Arrays.copyOf(types, types.length+10);
		types[size] = t;
		size++;
	}
	void clear(){
		size = 0;
	}
	byte get(int index){
		return types[index];
	}
	int size(){
		return size;
	}
}
