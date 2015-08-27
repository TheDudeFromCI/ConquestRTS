package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import java.io.File;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkHeightData{
	private final short[] heights = new short[64*64];
	private final byte[] biomes = new byte[64*64];
	private final int minHeight;
	private final int maxHeight;
	private final int x;
	private final int z;
	public ChunkHeightData(WorldNoiseMachine machine, int x, int z, MassChunkHeightData massChunkHeightData){
		this.x = x;
		this.z = z;
		File file = Algorithms.getChunkHeightsPath(x, z);
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			minHeight = bin.getInt();
			maxHeight = bin.getInt();
			for(int i = 0; i<heights.length; i++){
				heights[i] = bin.getShort();
				biomes[i] = bin.getByte();
			}
			return;
		}
		int u = Integer.MAX_VALUE;
		int v = Integer.MIN_VALUE;
		int a, b, index;
		Biome biome;
		float[] height = new float[3];
		int[] heights = new int[64*64];
		for(b = 0; b<64; b++)
			for(a = 0; a<64; a++){
				biome = machine.getBiomeAt(x+a, z+b, height);
				index = b*64+a;
				biomes[index] = (byte)biome.ordinal();
				heights[index] = WorldNoiseMachine.scaleHeight(biome, height[0], height[1], height[2]);
				if(heights[index]<u)
					u = heights[index];
				if(heights[index]>v)
					v = heights[index];
			}
		minHeight = u;
		maxHeight = v;
		for(int i = 0; i<heights.length; i++)
			this.heights[i] = (short)(heights[i]-minHeight);
		BinaryFile bin = new BinaryFile(3*64*64+8);
		bin.addInt(minHeight);
		bin.addInt(maxHeight);
		for(int i = 0; i<this.heights.length; i++){
			bin.addShort(this.heights[i]);
			bin.addByte(biomes[i]);
		}
		bin.compress(false);
		bin.compile(file);
		int[] h = new int[2];
		getChunkHeight(h);
		massChunkHeightData.setHeight(x, z, h);
	}
	public Biome getBiome(int x, int z){
		return Biome.values()[biomes[(z-this.z)*64+x-this.x]&0xFF];
	}
	public void getChunkHeight(int[] out){
		int minHeight = Algorithms.groupLocation(this.minHeight, LandscapeChunk.LandscapeSize);
		int maxHeight = Algorithms.groupLocation(this.maxHeight, LandscapeChunk.LandscapeSize);
		out[0] = minHeight;
		out[1] = (maxHeight-minHeight)/LandscapeChunk.LandscapeSize+1;
	}
	public int getHeight(int x, int z){
		return heights[(z-this.z)*64+x-this.x]+minHeight;
	}
	public int getX(){
		return x;
	}
	public int getZ(){
		return z;
	}
}
