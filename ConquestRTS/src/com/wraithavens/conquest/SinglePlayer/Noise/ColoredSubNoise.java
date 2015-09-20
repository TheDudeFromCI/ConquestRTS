package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.Utility.InterpolationFunction;

public class ColoredSubNoise{
	public static ColoredSubNoise build(long seed, float smoothness, int detail, InterpolationFunction function,
		float amplitude, float offset, Vector4f minColor, Vector4f maxColor,
		InterpolationFunction colorFunction, float minColorWeight, float maxColorWeight){
		NoiseGenerator n = new NoiseGenerator(seed, smoothness, detail);
		n.setFunction(function);
		return new ColoredSubNoise(n, amplitude, offset, minColor, maxColor, colorFunction, minColorWeight,
			maxColorWeight);
	}
	private final NoiseGenerator noise;
	private final float amplitude;
	private final float offset;
	private final Vector4f minColor;
	private final Vector4f maxColor;
	private final InterpolationFunction colorFunction;
	private final float totalColorWeight;
	private ColoredSubNoise(
		NoiseGenerator noise, float amplitude, float offset, Vector4f minColor, Vector4f maxColor,
		InterpolationFunction colorFunction, float minColorWeight, float maxColorWeight){
		this.noise = noise;
		this.amplitude = amplitude;
		this.offset = offset;
		this.minColor = minColor;
		this.maxColor = maxColor;
		this.colorFunction = colorFunction;
		totalColorWeight = minColorWeight+maxColorWeight;
		this.minColor.scale(minColorWeight);
		this.maxColor.scale(maxColorWeight);
	}
	/**
	 * Gets the noise, and color at this location. The the color is stored in
	 * the vector, and the height is returned as a float.
	 */
	float noise(float x, float y, float z, Vector4f colorOut){
		float real = noise.noise(x, y, z);
		float out = real*amplitude+offset;
		colorOut.set(colorFunction.interpolate(minColor.x, maxColor.x, real)/totalColorWeight,
			colorFunction.interpolate(minColor.y, maxColor.y, real)/totalColorWeight,
			colorFunction.interpolate(minColor.z, maxColor.z, real)/totalColorWeight,
			colorFunction.interpolate(minColor.w, maxColor.w, real)/totalColorWeight);
		return out;
	}
}
