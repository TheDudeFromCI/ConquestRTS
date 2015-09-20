package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

class MassChunkHeightData{
	private final short[] heightRanges = new short[128*128*2];
	private final int x;
	private final int z;
	MassChunkHeightData(int x, int z){
		this.x = x;
		this.z = z;
		File file = Algorithms.getMassChunkHeightsPath(x, z);
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			for(int i = 0; i<heightRanges.length; i += 2){
				heightRanges[i] = bin.getShort();
				heightRanges[i+1] = bin.getByte();
			}
		}
	}
	boolean getHeights(int x, int z, int[] out){
		int index = ((z-this.z)/64*128+(x-this.x)/64)*2;
		out[0] = heightRanges[index]*64;
		out[1] = heightRanges[index+1];
		return heightRanges[index+1]!=0;
	}
	int getX(){
		return x;
	}
	int getZ(){
		return z;
	}
	synchronized void setHeight(int x, int z, int[] in){
		int index = ((z-this.z)/64*128+(x-this.x)/64)*2;
		heightRanges[index] = (short)(Algorithms.groupLocation(in[0], 64)/64);
		heightRanges[index+1] = (short)in[1];
		File file = Algorithms.getMassChunkHeightsPath(this.x, this.z);
		BinaryFile bin = new BinaryFile(128*128*5);
		for(int i = 0; i<heightRanges.length; i += 2){
			bin.addShort(heightRanges[i]);
			bin.addByte((byte)heightRanges[i+1]);
		}
		bin.compress(false);
		bin.compile(file);
	}
}
