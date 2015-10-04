package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.util.Arrays;

public class VertexStorage{
	private float[] vertices = new float[700];
	private int size;
	public void clear(){
		size = 0;
	}
	public float get(int i){
		return vertices[i];
	}
	public float[] getAll(){
		return vertices;
	}
	public int place(float x, float y, float z, float shade, float tx, float ty, float tz){
		for(int i = 0; i<size; i += 7)
			if(vertices[i]==x&&vertices[i+1]==y&&vertices[i+2]==z&&vertices[i+3]==shade&&vertices[i+4]==tx
				&&vertices[i+5]==ty&&vertices[i+6]==tz)
				return i;
		if(size==vertices.length)
			vertices = Arrays.copyOf(vertices, vertices.length+700);
		vertices[size] = x;
		vertices[size+1] = y;
		vertices[size+2] = z;
		vertices[size+3] = shade;
		vertices[size+4] = tx;
		vertices[size+5] = ty;
		vertices[size+6] = tz;
		int originalSize = size;
		size += 7;
		return originalSize;
	}
	public int size(){
		return size;
	}
}
