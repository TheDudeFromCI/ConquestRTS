package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.LinearInterpolation;

public class WorldNoiseMachine{
	/**
	 * Created a world noise generation machine. The seeds determine how each
	 * part of the noise machine functions. This includes world height,
	 * humidity, tempature, etc.
	 *
	 * @param seeds
	 *            - An array of 7 longs which determine the seeds for each
	 *            component of the world.
	 * @return The world noise machine that generates a world under the
	 *         requested conditions.
	 */
	public static WorldNoiseMachine generate(long[] seeds){
		// ---
		// This will be the main control over how the world is generated. Based
		// on the seeds given, of course.
		// ---
		CosineInterpolation cos = new CosineInterpolation();
		LinearInterpolation lerp = new LinearInterpolation();
		SubNoise worldHeightNoise1 = SubNoise.build(seeds[0], 6000, 6, cos, 40000, 0);
		SubNoise worldHeightNoise2 = SubNoise.build(seeds[1], 300, 3, lerp, 800, 0);
		SubNoise humidityNoise1 = SubNoise.build(seeds[2], 7500, 10, cos, 1, 0);
		SubNoise tempatureNoise1 = SubNoise.build(seeds[3], 20000, 11, cos, 1, 0);
		SubNoise prairieRed = SubNoise.build(seeds[4], 120, 2, lerp, 0.15f, 0.25f);
		SubNoise prairieGreen = SubNoise.build(seeds[5], 20, 0, lerp, 0.1f, 0);
		SubNoise prairieBlue = SubNoise.build(seeds[6], 80, 2, lerp, 0.15f, 0.3f);
		// ---
		// And compiling these together.
		// ---
		AdvancedNoise worldHeight = new AdvancedNoise();
		worldHeight.addSubNoise(worldHeightNoise1);
		worldHeight.addSubNoise(worldHeightNoise2);
		AdvancedNoise humidity = new AdvancedNoise();
		humidity.addSubNoise(humidityNoise1);
		AdvancedNoise tempature = new AdvancedNoise();
		tempature.addSubNoise(tempatureNoise1);
		ColorNoise prairieColor = new ColorNoise(prairieRed, prairieGreen, prairieBlue);
		return new WorldNoiseMachine(worldHeight, humidity, tempature, prairieColor);
	}
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
	public double getMaxHeight(){
		return worldHeight.getMaxHeight();
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
