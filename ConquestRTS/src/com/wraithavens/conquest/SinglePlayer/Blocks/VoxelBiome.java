package com.wraithavens.conquest.SinglePlayer.Blocks;

import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class VoxelBiome extends VoxelChunk{
	private final BiomeMap biome;
	public VoxelBiome(WorldNoiseMachine machine, int x, int y, int z, int size){
		super(x, y, z, size);
		biome = new BiomeMap(machine, x, z);
	}
	void bind(){
		biome.bind();
	}
	void dispose(){
		biome.dispose();
	}
}
