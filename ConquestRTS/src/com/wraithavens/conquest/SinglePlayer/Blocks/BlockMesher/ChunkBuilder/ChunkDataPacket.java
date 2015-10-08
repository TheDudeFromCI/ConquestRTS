package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

public class ChunkDataPacket{
	public static final int GrassDataPacket = 0;
	public static final int MeshDataPacket = 1;
	public static final int BiomeColorDataPacket = 2;
	public static final int EntityDataPacket = 3;
	protected final byte[] data;
	private final int packetType;
	public ChunkDataPacket(int packetType, byte[] data){
		this.data = data;
		this.packetType = packetType;
	}
	public byte[] getBytes(){
		return data;
	}
	public int getPacketType(){
		return packetType;
	}
	public int size(){
		return data.length;
	}
}
