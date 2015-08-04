package com.wraithavens.conquest.SinglePlayer.Blocks;

import org.lwjgl.opengl.GL13;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class VoxelBiome extends VoxelChunk{
	private final BiomeMap biome;
	public VoxelBiome(WorldNoiseMachine machine, int x, int y, int z, int size){
		super(x, y, z, size);
		biome = new BiomeMap(machine, x, z);
	}
	public void dispose(){
		biome.dispose();
	}
	void bind(){
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		biome.bind();
	}
}
