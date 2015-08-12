package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Entities.EntityType;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.LinearInterpolation;

public class WorldNoiseMachine{
	/**
	 * Created a world noise generation machine. The seeds determine how each
	 * part of the noise machine functions. This includes world height,
	 * humidity, tempature, etc.
	 *
	 * @param seeds
	 *            - An array of 4 longs which determine the seeds for each
	 *            component of the world.
	 * @return The world noise machine that generates a world under the
	 *         requested conditions.
	 */
	public static WorldNoiseMachine generate(long[] seeds){
		GlError.out("Making world noise machine.");
		// ---
		// This will be the main control over how the world is generated. Based
		// on the seeds given, of course.
		// ---
		CosineInterpolation cos = new CosineInterpolation();
		LinearInterpolation lerp = new LinearInterpolation();
		SubNoise worldHeightNoise1 = SubNoise.build(seeds[0], 6000, 6, cos, 5000, 0);
		SubNoise prairieRed = SubNoise.build(seeds[1], 120, 2, lerp, 0.15f, 0.25f);
		SubNoise prairieGreen = SubNoise.build(seeds[2], 20, 1, lerp, 0.1f, 0);
		SubNoise prairieBlue = SubNoise.build(seeds[3], 80, 2, lerp, 0.15f, 0.3f);
		// ---
		// And compiling these together.
		// ---
		AdvancedNoise worldHeight = new AdvancedNoise();
		worldHeight.addSubNoise(worldHeightNoise1);
		ColorNoise prairieColor = new ColorNoise(prairieRed, prairieGreen, prairieBlue);
		return new WorldNoiseMachine(worldHeight, prairieColor);
	}
	// ---
	// General world-wide generators.
	// ---
	private final AdvancedNoise worldHeight;
	// ---
	// Biome specific noise generators.
	// ---
	private final ColorNoise prairieColor;
	private WorldNoiseMachine(AdvancedNoise worldHeight, ColorNoise prairieColor){
		this.worldHeight = worldHeight;
		this.prairieColor = prairieColor;
	}
	public double getMaxHeight(){
		return worldHeight.getMaxHeight();
	}
	public void getPrairieColor(float x, float y, Vector3f colorOut){
		prairieColor.noise(x, y, colorOut);
	}
	public double getWorldHeight(float x, float y){
		return worldHeight.noise(x, y);
	}
	@SuppressWarnings({
		"static-method", "unused"
	})
	public EntityType randomPlant(int x, int y){
		if(Math.random()<0.1){
			int i = (int)(Math.random()*3);
			if(i==0)
				return EntityType.Grass1;
			if(i==1)
				return EntityType.Grass2;
			return EntityType.Grass3;
		}
		return null;
	}
}
