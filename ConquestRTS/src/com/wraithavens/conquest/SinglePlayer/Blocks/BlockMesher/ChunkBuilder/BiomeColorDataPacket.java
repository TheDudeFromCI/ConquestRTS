package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;

public class BiomeColorDataPacket extends ChunkDataPacket{
	public BiomeColorDataPacket(byte[] data){
		super(ChunkDataPacket.BiomeColorDataPacket, data);
	}
	public BiomeColorDataPacket(ByteBuffer colorData){
		super(ChunkDataPacket.BiomeColorDataPacket, colorData.array());
	}
	public ByteBuffer getPixelData(){
		ByteBuffer pixels = BufferUtils.createByteBuffer(data.length);
		pixels.put(data);
		pixels.flip();
		return pixels;
	}
}
