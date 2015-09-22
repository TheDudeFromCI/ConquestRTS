package com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise;

import com.wraithavens.conquest.SinglePlayer.Noise.NoiseGenerator;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.LinearInterpolation;

public class ArcstoneHillsNoise implements BiomeNoiseMachine{
	private final NoiseGenerator noise;
	private final NoiseGenerator noise2;
	private final NoiseGenerator noise3;
	private final NoiseGenerator noise4;
	public ArcstoneHillsNoise(long seed){
		CosineInterpolation cos = new CosineInterpolation();
		LinearInterpolation lerp = new LinearInterpolation();
		noise = new NoiseGenerator(seed, 7000, 8);
		noise.setFunction(cos);
		noise2 = new NoiseGenerator(seed*33857^35023, 1000, 4);
		noise2.setFunction(cos);
		noise3 = new NoiseGenerator(seed*24809^36761, 300, 3);
		noise3.setFunction(cos);
		noise4 = new NoiseGenerator(seed*33857^35023, 1000, 5); // Exact copy of
		// noise2,
		// except linear
		// noise, and slightly higher detail.
		noise4.setFunction(lerp);
	}
	public float getHeight(float x, float z){
		float n = noise.noise(x, z)*3000+noise3.noise(x, z)*1000;
		float n2 = noise2.noise(x, z)-0.8f;
		if(n2>0)
			n += n2*5*1000+(noise4.noise(x, z)-0.8f)*100;
		return n;
	}
}
