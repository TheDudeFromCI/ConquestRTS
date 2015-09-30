package com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise;

import com.wraithavens.conquest.SinglePlayer.Noise.NoiseGenerator;
import com.wraithavens.conquest.Utility.CosineInterpolation;

public class ArcstoneHillsNoise implements BiomeNoiseMachine{
	private final NoiseGenerator noise;
	private final NoiseGenerator noise2;
	public ArcstoneHillsNoise(long seed){
		CosineInterpolation cos = new CosineInterpolation();
		noise = new NoiseGenerator(seed, 300, 3);
		noise.setFunction(cos);
		noise2 = new NoiseGenerator(seed+100, 80, 3);
		noise2.setFunction(cos);
	}
	public float getHeight(float x, float z){
		return noise.noise(x, z)*1300+noise2.noise(x, z)*500;
	}
}
