package com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise;

import com.wraithavens.conquest.SinglePlayer.Noise.NoiseGenerator;
import com.wraithavens.conquest.Utility.CosineInterpolation;

public class TayleaMeadowNoise implements BiomeNoiseMachine{
	private final NoiseGenerator noise;
	public TayleaMeadowNoise(long seed){
		noise = new NoiseGenerator(seed, 300, 3);
		noise.setFunction(new CosineInterpolation());
	}
	public float getHeight(float x, float z){
		return noise.noise(x, z)*50;
	}
}
