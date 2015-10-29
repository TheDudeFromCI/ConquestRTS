package com.wraithavens.conquest.SinglePlayer.Blocks.World;

public class ChunkStackBuilderEntityData{
	private final float[] data;
	private final int type;
	public ChunkStackBuilderEntityData(float[] data, int type){
		this.data = data;
		this.type = type;
	}
	float[] getData(){
		return data;
	}
	int getType(){
		return type;
	}
}
