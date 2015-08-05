package com.wraithavens.conquest.SinglePlayer.Skybox;

import com.wraithavens.conquest.Math.Vector4f;
import com.wraithavens.conquest.SinglePlayer.Noise.CloudNoise;
import com.wraithavens.conquest.SinglePlayer.Noise.ColoredSubNoise;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.InterpolationFunction;
import com.wraithavens.conquest.Utility.LinearInterpolation;

public class SkyboxBuilder{
	private static class SubNoisePart{
		private long seed = 0;
		private float smoothness = 1.0f;
		private int detail = 1;
		private InterpolationFunction function = Lerp;
		private float amplitude = 1.0f;
		private float offset = 0.0f;
		private Vector4f minColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);
		private Vector4f maxColor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
		private InterpolationFunction colorFunction = Lerp;
		private float minColorWeight = 1.0f;
		private float maxColorWeight = 1.0f;
	}
	public static final LinearInterpolation Lerp = new LinearInterpolation();
	public static final CosineInterpolation Cerp = new CosineInterpolation();
	private final CloudNoise noise = new CloudNoise();
	private SubNoisePart part;
	private boolean backdrop;
	public SkyboxBuilder(){
		newSubNoise();
	}
	public SkyboxClouds build(){
		addPart();
		return new SkyboxClouds(noise, backdrop);
	}
	public void newSubNoise(){
		addPart();
		part = new SubNoisePart();
	}
	public void reset(){
		part = null;
		noise.clear();
		newSubNoise();
	}
	public void setAmplitude(float amplitude){
		part.amplitude = amplitude;
	}
	public void setBackdrop(boolean backdrop){
		this.backdrop = backdrop;
	}
	public void setColorFunction(InterpolationFunction colorFunction){
		part.colorFunction = colorFunction;
	}
	public void setDetail(int detail){
		part.detail = detail;
	}
	public void setFunction(InterpolationFunction function){
		part.function = function;
	}
	public void setMaxColor(Vector4f maxColor){
		part.maxColor = maxColor;
	}
	public void setMaxColorWeight(float maxColorWeight){
		part.maxColorWeight = maxColorWeight;
	}
	public void setMinColor(Vector4f minColor){
		part.minColor = minColor;
	}
	public void setMinColorWeight(float minColorWeight){
		part.minColorWeight = minColorWeight;
	}
	public void setOffset(float offset){
		part.offset = offset;
	}
	public void setSeed(long seed){
		part.seed = seed;
	}
	public void setSmoothness(float smoothness){
		part.smoothness = smoothness;
	}
	private void addPart(){
		if(part!=null){
			noise.addSubNoise(ColoredSubNoise.build(part.seed, part.smoothness, part.detail, part.function,
				part.amplitude, part.offset, part.minColor, part.maxColor, part.colorFunction,
				part.minColorWeight, part.maxColorWeight));
			part = null;
		}
	}
}
