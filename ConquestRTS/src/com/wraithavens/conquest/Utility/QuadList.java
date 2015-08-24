package com.wraithavens.conquest.Utility;

import java.util.Arrays;
import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Quad;

public class QuadList{
	private Quad[] quads = new Quad[100];
	private int size;
	public void add(Quad q){
		if(size==quads.length)
			quads = Arrays.copyOf(quads, quads.length+100);
		quads[size] = q;
		size++;
	}
	public void clear(){
		size = 0;
	}
	public Quad get(int index){
		return quads[index];
	}
	public int size(){
		return size;
	}
}
