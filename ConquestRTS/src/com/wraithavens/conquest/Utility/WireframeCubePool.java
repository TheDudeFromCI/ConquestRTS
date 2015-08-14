package com.wraithavens.conquest.Utility;

import java.util.Arrays;

public class WireframeCubePool{
	private WireframeCubePart[] list = new WireframeCubePart[0];
	private int pos = 0;
	public void dump(){
		list = new WireframeCubePart[0];
		pos = 0;
	}
	public WireframeCubePart get(){
		if(pos==list.length){
			list = Arrays.copyOf(list, list.length+1);
			WireframeCubePart part = new WireframeCubePart();
			list[pos] = part;
			pos++;
			return part;
		}
		WireframeCubePart part = list[pos];
		pos++;
		return part;
	}
	public void reset(){
		pos = 0;
	}
	public int size(){
		return list.length;
	}
}
