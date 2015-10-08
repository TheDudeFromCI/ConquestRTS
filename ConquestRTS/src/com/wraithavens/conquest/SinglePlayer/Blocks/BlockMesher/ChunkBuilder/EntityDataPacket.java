package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.EntityDataRaw;
import com.wraithavens.conquest.SinglePlayer.Entities.AesiaStem;
import com.wraithavens.conquest.SinglePlayer.Entities.Entity;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.Utility.ByteFormatter;

public class EntityDataPacket extends ChunkDataPacket{
	private static byte[] compile(ArrayList<EntityDataRaw> entities){
		int size = entities.size()*6*4;
		byte[] bytes = new byte[size];
		ByteFormatter b = new ByteFormatter(bytes);
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
	private ByteFormatter b;
	public EntityDataPacket(ArrayList<EntityDataRaw> entities){
		super(ChunkDataPacket.EntityDataPacket, compile(entities));
	}
	public EntityDataPacket(byte[] data){
		super(ChunkDataPacket.EntityDataPacket, data);
	}
	public boolean hasNext(){
		return b.hasNext();
	}
	public Entity next(){
		EntityType type = EntityType.values()[b.getInt()];
		Entity e;
		if(type.isType(EntityType.AesiaStems, 24))
			e = new AesiaStem(type);
		else
			e = new Entity(type);
		e.moveTo(b.getFloat(), b.getFloat(), b.getFloat());
		e.setYaw(b.getFloat());
		e.scaleTo(b.getFloat());
		e.updateAABB();
		return e;
	}
	public void prepareEntityIterator(){
		b = new ByteFormatter(data);
	}
}
