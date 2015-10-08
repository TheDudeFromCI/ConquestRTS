package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.EntityDataRaw;
import com.wraithavens.conquest.Utility.ByteFormatter;

public class EntityDataPacket extends ChunkDataPacket{
	private static byte[] compile(ArrayList<EntityDataRaw> entities){
		int size = entities.size()*6*4+4;
		byte[] bytes = new byte[size];
		ByteFormatter b = new ByteFormatter(bytes);
		b.addInt(entities.size());
		for(EntityDataRaw data : entities){
			b.addInt(data.getType());
			b.addFloat(data.getX());
			b.addFloat(data.getY());
			b.addFloat(data.getZ());
			b.addFloat(data.getR());
			b.addFloat(data.getS());
		}
		return bytes;
	}
	public EntityDataPacket(ArrayList<EntityDataRaw> entities){
		super(ChunkDataPacket.EntityDataPacket, compile(entities));
	}
	public EntityDataPacket(byte[] data){
		super(ChunkDataPacket.EntityDataPacket, data);
	}
}
