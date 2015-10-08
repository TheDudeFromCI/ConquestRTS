package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.BufferUtils;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshRenderer;
import com.wraithavens.conquest.SinglePlayer.Entities.Water.WaterPuddle;
import com.wraithavens.conquest.Utility.ByteFormatter;

public class MeshDataPacket extends ChunkDataPacket{
	private static byte[] compile(MeshRenderer renderer){
		FloatBuffer vertexData = renderer.getVertexData();
		ShortBuffer indexData = renderer.getIndexData();
		FloatBuffer waterVertexData = renderer.getWaterVertexData();
		ShortBuffer waterIndexData = renderer.getWaterIndexData();
		boolean hasWater = waterVertexData!=null;
		int size = 0;
		size += vertexData.capacity()*4+4;
		size += indexData.capacity()*2+4;
		size += 1;
		if(hasWater){
			size += waterVertexData.capacity()*4+4;
			size += waterIndexData.capacity()*2+4;
		}
		byte[] bytes = new byte[size];
		ByteFormatter b = new ByteFormatter(bytes);
		b.addInt(vertexData.capacity());
		b.addInt(indexData.capacity());
		while(vertexData.hasRemaining())
			b.addFloat(vertexData.get());
		while(indexData.hasRemaining())
			b.addShort(indexData.get());
		b.addBoolean(hasWater);
		if(hasWater){
			b.addInt(waterVertexData.capacity());
			b.addInt(waterIndexData.capacity());
			while(waterVertexData.hasRemaining())
				b.addFloat(waterVertexData.get());
			while(waterIndexData.hasRemaining())
				b.addShort(waterIndexData.get());
		}
		return bytes;
	}
	private FloatBuffer vertexData;
	private ShortBuffer indexData;
	private FloatBuffer waterVertexData;
	private ShortBuffer waterIndexData;
	public MeshDataPacket(byte[] data){
		super(ChunkDataPacket.MeshDataPacket, data);
	}
	public MeshDataPacket(MeshRenderer renderer){
		super(ChunkDataPacket.MeshDataPacket, compile(renderer));
	}
	public void decompile(){
		ByteFormatter b = new ByteFormatter(data);
		vertexData = BufferUtils.createFloatBuffer(b.getInt());
		indexData = BufferUtils.createShortBuffer(b.getInt());
		while(vertexData.hasRemaining())
			vertexData.put(b.getFloat());
		vertexData.flip();
		while(indexData.hasRemaining())
			indexData.put(b.getShort());
		indexData.flip();
		if(b.getBoolean()){
			waterVertexData = BufferUtils.createFloatBuffer(b.getInt());
			waterIndexData = BufferUtils.createShortBuffer(b.getInt());
			while(waterVertexData.hasRemaining())
				waterVertexData.put(b.getFloat());
			waterVertexData.flip();
			while(waterIndexData.hasRemaining())
				waterIndexData.put(b.getShort());
			waterIndexData.flip();
		}
	}
	public ShortBuffer getIndexData(){
		return indexData;
	}
	public FloatBuffer getVertexData(){
		return vertexData;
	}
	public WaterPuddle getWaterPuddle(int x, int y, int z){
		if(waterIndexData==null)
			return null;
		return new WaterPuddle(waterVertexData, waterIndexData, x, y, z);
	}
}
