package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshRenderer{
	private final FloatBuffer vertexData;
	private final ShortBuffer indexData;
	private final FloatBuffer waterVertexData;
	private final ShortBuffer waterIndexData;
	MeshRenderer(
		FloatBuffer vertexData, ShortBuffer indexData, FloatBuffer waterVertexData, ShortBuffer waterIndexData){
		this.vertexData = vertexData;
		this.indexData = indexData;
		this.waterVertexData = waterVertexData;
		this.waterIndexData = waterIndexData;
	}
	public ShortBuffer getIndexData(){
		return indexData;
	}
	public FloatBuffer getVertexData(){
		return vertexData;
	}
	public ShortBuffer getWaterIndexData(){
		return waterIndexData;
	}
	public FloatBuffer getWaterVertexData(){
		return waterVertexData;
	}
}
