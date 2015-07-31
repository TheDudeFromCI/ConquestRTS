package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Utility.InterpolationFunction;
import com.wraithavens.conquest.Utility.NoiseGenerator;

public class SubNoise{
	public static SubNoise build(long seed, float smoothness, int detail, InterpolationFunction function,
		float amplitude, float offset){
		NoiseGenerator n = new NoiseGenerator(seed, smoothness, detail);
		n.setFunction(function);
		return new SubNoise(n, amplitude, offset);
	}
	public final NoiseGenerator noise;
	public final float amplitude;
	public final float offset;
	public SubNoise(NoiseGenerator noise, float amplitude, float offset){
		this.noise = noise;
		this.amplitude = amplitude;
		this.offset = offset;
	}
	public double noise(float x, float y){
		return noise.noise(x, y)*amplitude+offset;
	}
}
