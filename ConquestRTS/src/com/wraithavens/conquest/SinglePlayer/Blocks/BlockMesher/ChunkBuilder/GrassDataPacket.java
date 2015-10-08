package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.ChunkBuilder;

import java.util.ArrayList;
import com.wraithavens.conquest.SinglePlayer.Blocks.Landscape.GrassDataRaw;

public class GrassDataPacket extends ChunkDataPacket{
	private static int addFloat(byte[] bytes, int pos, float f){
		int i = Float.floatToIntBits(f);
		bytes[pos] = (byte)(i&0xFF);
		bytes[pos+1] = (byte)(i>>8&0xFF);
		bytes[pos+2] = (byte)(i>>16&0xFF);
		bytes[pos+3] = (byte)(i>>24&0xFF);
		return pos+4;
	}
	private static byte[] compile(int type, ArrayList<GrassDataRaw> grassLocations){
		byte[] bytes = new byte[grassLocations.size()*8*4+1];
		bytes[0] = (byte)type;
		int pos = 1;
		for(GrassDataRaw data : grassLocations){
			pos = addFloat(bytes, pos, data.getX());
			pos = addFloat(bytes, pos, data.getY());
			pos = addFloat(bytes, pos, data.getZ());
			pos = addFloat(bytes, pos, data.getR());
			pos = addFloat(bytes, pos, data.getS());
			pos = addFloat(bytes, pos, data.getRed());
			pos = addFloat(bytes, pos, data.getGreen());
			pos = addFloat(bytes, pos, data.getBlue());
		}
		return bytes;
	}
	public GrassDataPacket(byte[] data){
		super(ChunkDataPacket.GrassDataPacket, data);
	}
	public GrassDataPacket(int type, ArrayList<GrassDataRaw> grassLocations){
		super(ChunkDataPacket.GrassDataPacket, compile(type, grassLocations));
	}
}
