package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;

public class WorldNoiseMachine{
	// ---
	// General world-wide generators.
	// ---
	private final AdvancedNoise worldHeight;
	private final AdvancedNoise humidity;
	private final AdvancedNoise tempature;
	// ---
	// Biome specific noise generators.
	// ---
	private final ColorNoise prairieColor;
	public WorldNoiseMachine(
		AdvancedNoise worldHeight, AdvancedNoise humidity, AdvancedNoise tempature, ColorNoise prairieColor){
		this.worldHeight = worldHeight;
		this.humidity = humidity;
		this.tempature = tempature;
		this.prairieColor = prairieColor;
	}
	public double getHumidity(float x, float y){
		return humidity.noise(x, y);
	}
	public double getNormalisedWorldHeight(float x, float y){
		return worldHeight.normalisedNoise(x, y);
	}
	public void getPrairieColor(float x, float y, Vector3f colorOut){
		prairieColor.noise(x, y, colorOut);
	}
	public double getTempature(float x, float y){
		return tempature.noise(x, y);
	}
	public double getWorldHeight(float x, float y){
		return worldHeight.noise(x, y);
	}
}
