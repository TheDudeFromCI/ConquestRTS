package com.wraithavens.conquest.SinglePlayer.Entities.DynmapEntities;

import java.io.File;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.BinaryFile;

public class GiantEntityGroundHits{
	private static boolean getBit(byte b, int index){
		return (b>>index&1)==1;
	}
	private static String sub(String s, int end){
		return s.substring(0, s.length()-end);
	}
	private final boolean[] usesBlock;
	private final int sizeX;
	private final int sizeZ;
	private final int offsetX;
	private final int offsetZ;
	private final GroundHitCoords hits = new GroundHitCoords();
	public GiantEntityGroundHits(EntityType type){
		File file = new File(WraithavensConquest.modelFolder, sub(type.fileName, 3)+"egt");
		if(!file.exists()){
			sizeX = 1;
			sizeZ = 1;
			offsetX = 0;
			offsetZ = 0;
			usesBlock = new boolean[1];
			usesBlock[0] = true;
			return;
		}
		BinaryFile bin = new BinaryFile(file);
		bin.decompress(false);
		sizeX = bin.getInt();
		sizeZ = bin.getInt();
		offsetX = bin.getInt();
		offsetZ = bin.getInt();
		usesBlock = new boolean[sizeX*sizeZ];
		byte[] bytes = new byte[(int)Math.ceil(usesBlock.length/8f)];
		bin.getBytes(bytes);
		for(int i = 0; i<usesBlock.length; i++)
			usesBlock[i] = getBit(bytes[i/8], i%8);
	}
	public int getGround(WorldNoiseMachine machine, float x, float z, float r, float s){
		hits.clear();
		int lowest = Integer.MAX_VALUE;
		int a, b;
		int tempZIndex;
		float sin = (float)Math.sin(r);
		float cos = (float)Math.cos(r);
		for(b = 0; b<sizeZ; b++){
			tempZIndex = b*sizeX;
			for(a = 0; a<sizeX; a++)
				if(usesBlock[tempZIndex+a])
					addBlock(a+offsetX, b+offsetZ, s, sin, cos, x, z);
		}
		int size = hits.getSize();
		int height;
		for(int i = 0; i<size; i++){
			height = machine.getGroundLevel(hits.getX(i), hits.getZ(i));
			if(height<lowest)
				lowest = height;
		}
		return lowest-1;
	}
	private void addBlock(int subX, int subZ, float s, float sin, float cos, float originX, float originZ){
		int x2 = subX+1;
		int z2 = subZ+1;
		hits.place((int)Math.floor((subX*cos-subZ*sin)*s+originX),
			(int)Math.floor((subX*sin+subZ*cos)*s+originZ));
		hits.place((int)Math.floor((x2*cos-subZ*sin)*s+originX), (int)Math.floor((x2*sin+subZ*cos)*s+originZ));
		hits.place((int)Math.floor((subX*cos-z2*sin)*s+originX), (int)Math.floor((subX*sin+z2*cos)*s+originZ));
		hits.place((int)Math.floor((x2*cos-z2*sin)*s+originX), (int)Math.floor((x2*sin+z2*cos)*s+originZ));
	}
}
