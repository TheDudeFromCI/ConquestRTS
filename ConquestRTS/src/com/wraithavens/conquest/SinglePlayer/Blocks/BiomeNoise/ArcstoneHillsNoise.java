package com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise;

import com.wraithavens.conquest.SinglePlayer.Noise.NoiseGenerator;
import com.wraithavens.conquest.Utility.CosineInterpolation;

public class ArcstoneHillsNoise implements BiomeNoiseMachine{
	private final NoiseGenerator noise;
	public ArcstoneHillsNoise(long seed){
		noise = new NoiseGenerator(seed, 7000, 8);
		noise.setFunction(new CosineInterpolation());
	}
	public float getHeight(float x, float z){
		return noise.noise(x, z)*3000;
	}
}
