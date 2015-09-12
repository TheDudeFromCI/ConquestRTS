package com.wraithavens.conquest.WorldGeneration.ChunkGeneration;

import java.io.File;
import com.wraithavens.conquest.SinglePlayer.Noise.Biome;
import com.wraithavens.conquest.Utility.BinaryFile;
import com.wraithavens.conquest.WorldGeneration.World;

class ChunkStub{
	private static float[] temp = new float[2];
	private final int x;
	private final int z;
	private final File file;
	private final World world;
	private int layer;
	private float[] worldStats;
	private Biome[] biomes;
	private byte[] blocks;
	ChunkStub(World world, int x, int z, File file){
		this.x = x;
		this.z = z;
		this.file = file;
		this.world = world;
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			layer = bin.getInt();
			biomes = new Biome[64*64];
			for(int i = 0; i<biomes.length; i++)
				biomes[i] = Biome.values()[bin.getUnsignedByte()];
			worldStats = new float[64*64*3];
			for(int i = 0; i<worldStats.length; i++)
				worldStats[i] = bin.getFloat();
		}else
			layer = -1;
	}
	private void save(){
		int totalBytes = biomes.length+worldStats.length*4+4;
		BinaryFile bin = new BinaryFile(totalBytes);
		bin.addInt(layer);
		for(Biome b : biomes)
			bin.addByte((byte)b.ordinal());
		for(float f : worldStats)
			bin.addFloat(f);
		bin.compress(true);
		bin.compile(file);
	}
	private void setBlock(int x, int y, int z, boolean block){
		int a = x*64*64+y*64+z;
		int b = a/8;
		int c = a%8;
		blocks[b] = blocks[b];
	}
	int getLayer(){
		return layer;
	}
	int getX(){
		return x;
	}
	int getZ(){
		return z;
	}
	@SuppressWarnings("unused")
	void update(ChunkStub[] allStubs, int chunkX, int chunkZ, int viewLength){
		layer++;
		if(layer==0){
			int x, z;
			biomes = new Biome[64*64];
			worldStats = new float[64*64*3];
			int tempZIndex, tempX, tempZ;
			int i = 0;
			for(z = 0; z<64; z++){
				tempZIndex = z*64;
				tempZ = z+this.z;
				for(x = 0; x<64; x++){
					tempX = x+this.x;
					world.getBiomePainter().getHTL(tempX, tempZ, temp);
					worldStats[i] = temp[0];
					worldStats[i+1] = temp[1];
					worldStats[i+2] = temp[2];
					biomes[tempZIndex+x] = Biome.getFittingBiome(temp[0], temp[1]);
					i += 3;
				}
			}
		}
		if(layer==1){
			blocks = new byte[64*64*64/8];
		}
		save();
	}
}
