package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.io.File;
import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.BiomeColorDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.ChunkDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.EntityDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.GrassDataPacket;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder.MeshDataPacket;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkStackBuilder{
	private final ArrayList<ChunkDataPacket> packets = new ArrayList();
	public void dispose(){
		packets.clear();
	}
	public ArrayList<ChunkDataPacket> getPackets(){
		return packets;
	}
	public void load(int x, int z){
		File file = Algorithms.getChunkStackPath(x, z);
		BinaryFile bin = new BinaryFile(file);
		bin.decompress(false);
		int packetCount = bin.getInt();
		byte[] data;
		int type;
		for(int i = 0; i<packetCount; i++){
			type = bin.getByte();
			data = new byte[bin.getInt()];
			bin.getBytes(data);
			switch(type){
				case ChunkDataPacket.MeshDataPacket:
					packets.add(new MeshDataPacket(data));
					break;
				case ChunkDataPacket.EntityDataPacket:
					packets.add(new EntityDataPacket(data));
					break;
				case ChunkDataPacket.GrassDataPacket:
					packets.add(new GrassDataPacket(data));
					break;
				case ChunkDataPacket.BiomeColorDataPacket:
					packets.add(new BiomeColorDataPacket(data));
					break;
				default:
					throw new RuntimeException();
			}
		}
	}
	public void save(int x, int y, int z){
		File file = Algorithms.getChunkPath(x, y, z);
		BinaryFile bin = new BinaryFile(getSize());
		bin.addInt(packets.size());
		for(ChunkDataPacket packet : packets){
			bin.addByte((byte)packet.getPacketType());
			bin.addInt(packet.size());
			bin.addBytes(packet.getBytes(), 0, packet.size());
		}
		bin.compress(false);
		bin.compile(file);
		packets.clear();
	}
	private int getSize(){
		int size = 4;
		for(ChunkDataPacket packet : packets)
			size += packet.size()+8;
		return size;
	}
}
