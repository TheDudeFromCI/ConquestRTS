package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.util.Arrays;

public class BlockTypeList{
	private byte[] types = new byte[10];
	private int size;
	public void add(byte t){
		for(int i = 0; i<size; i++)
			if(types[i]==t)
				return;
		if(size==types.length)
			types = Arrays.copyOf(types, types.length+10);
		types[size] = t;
		size++;
	}
	public void clear(){
		size = 0;
	}
	public byte get(int index){
		return types[index];
	}
	public int size(){
		return size;
	}
}
