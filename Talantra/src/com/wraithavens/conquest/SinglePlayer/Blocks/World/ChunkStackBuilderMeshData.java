package com.wraithavens.conquest.SinglePlayer.Blocks.World;

public class ChunkStackBuilderMeshData{
	private final float[] vertexData;
	private final short[] indexData;
	private final int y;
	public ChunkStackBuilderMeshData(float[] vertexData, short[] indexData, int y){
		this.vertexData = vertexData;
		this.indexData = indexData;
		this.y = y;
	}
	short[] getIndexData(){
		return indexData;
	}
	float[] getVertexData(){
		return vertexData;
	}
	int getY(){
		return y;
	}
}
