package com.wraithavens.conquest.WorldGeneration.ChunkGeneration;

class ChunkGenerationData{
	private final int x;
	private final int z;
	private int layer;
	ChunkGenerationData(int x, int z){
		this.x = x;
		this.z = z;
		layer = 1;
	}
	int getLayer(){
		return layer;
	}
	int getX(){
		return x;
	}
	int getZ(){
		return z;
	}
	void updateLayer(){
		layer++;
	}
}
