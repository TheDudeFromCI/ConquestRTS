package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshRenderer{
	private final FloatBuffer vertexData;
	private final ShortBuffer indexData;
	MeshRenderer(FloatBuffer vertexData, ShortBuffer indexData){
		this.vertexData = vertexData;
		this.indexData = indexData;
	}
	public ShortBuffer getIndexData(){
		return indexData;
	}
	public FloatBuffer getVertexData(){
		return vertexData;
	}
}
