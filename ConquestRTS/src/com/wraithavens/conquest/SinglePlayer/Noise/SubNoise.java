package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Utility.InterpolationFunction;

public class SubNoise{
	public static SubNoise build(long seed, float smoothness, int detail, InterpolationFunction function,
		float amplitude, float offset){
		NoiseGenerator n = new NoiseGenerator(seed, smoothness, detail);
		n.setFunction(function);
		return new SubNoise(n, amplitude, offset);
	}
	private final NoiseGenerator noise;
	final float amplitude;
	private final float offset;
	private SubNoise(NoiseGenerator noise, float amplitude, float offset){
		this.noise = noise;
		this.amplitude = amplitude;
		this.offset = offset;
	}
	double noise(float x, float y){
		return noise.noise(x, y)*amplitude+offset;
	}
	double noise(float x, float y, float z){
		return noise.noise(x, y, z)*amplitude+offset;
	}
}
