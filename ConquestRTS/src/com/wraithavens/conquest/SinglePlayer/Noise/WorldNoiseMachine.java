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
		SubNoise worldHeightNoise1 = SubNoise.build(seeds[0], 6000, 6, cos, 1500, 0);
		SubNoise prairieRed = SubNoise.build(seeds[1], 120, 2, lerp, 0.05f, 0.1f);
		SubNoise prairieGreen = SubNoise.build(seeds[2], 20, 1, lerp, 0.2f, 0.8f);
		SubNoise prairieBlue = SubNoise.build(seeds[3], 80, 2, lerp, 0.05f, 0.1f);
		SubNoise humidityNoise = SubNoise.build(seeds[4], 6000, 6, cos, 1, 0);
		SubNoise tempatureNoise = SubNoise.build(seeds[5], 6000, 6, cos, 1, 0);
		// ---
		// And compiling these together.
		// ---
		AdvancedNoise worldHeight = new AdvancedNoise();
		worldHeight.addSubNoise(worldHeightNoise1);
		ColorNoise prairieColor = new ColorNoise(prairieRed, prairieGreen, prairieBlue);
		AdvancedNoise humidity = new AdvancedNoise();
		humidity.addSubNoise(humidityNoise);
		AdvancedNoise tempature = new AdvancedNoise();
		tempature.addSubNoise(tempatureNoise);
		return new WorldNoiseMachine(worldHeight, prairieColor, humidity, tempature);
	}
	// ---
	// General world-wide generators.
	// ---
	private final AdvancedNoise worldHeight;
	private final AdvancedNoise tempature;
	private final AdvancedNoise humidity;
	// ---
	// Biome specific noise generators.
	// ---
	private final ColorNoise prairieColor;
	private NoiseGenerator grassShadeNoise = new NoiseGenerator(100, 20, 2);
	private WorldNoiseMachine(
		AdvancedNoise worldHeight, ColorNoise prairieColor, AdvancedNoise humidity, AdvancedNoise tempature){
		this.worldHeight = worldHeight;
		this.prairieColor = prairieColor;
		this.humidity = humidity;
		this.tempature = tempature;
		grassShadeNoise.setFunction(new CosineInterpolation());
	}
	public Biome getBiomeAt(int x, int z){
		float h = (float)humidity.noise(x, z);
		float t = (float)tempature.noise(x, z);
		float l = (float)(getWorldHeight(x, z)/getMaxHeight());
		return Biome.getFittingBiome(h, t, l);
	}
	@SuppressWarnings("unused")
	public void getBiomeColorAt(int x, int y, int z, Vector3f colorOut){
		float n = grassShadeNoise.noise(x, z)*-10;
		colorOut.set((109+n)/255f, (135+n)/255f, (24+n)/255f);
	}
	public int getGroundLevel(int x, int z){
		return (int)getWorldHeight(x+0.5f, z+0.5f);
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
	public EntityType randomPlant(int x, int z){
		if(Math.random()<0.2){
			if(Math.random()<0.02)
				return EntityType.TayleaFlower;
			if(Math.random()<0.0025)
				return EntityType.VallaFlower;
			if(Math.random()<0.005){
				int i = (int)(Math.random()*3);
				switch(i){
					case 0:
						return EntityType.Rock1;
					case 1:
						return EntityType.Rock2;
					case 2:
						return EntityType.Rock3;
					default:
						throw new AssertionError();
				}
			}
			int i = (int)(Math.random()*8);
			switch(i){
				case 0:
					return EntityType.Grass0;
				case 1:
					return EntityType.Grass1;
				case 2:
					return EntityType.Grass2;
				case 3:
					return EntityType.Grass3;
				case 4:
					return EntityType.Grass4;
				case 5:
					return EntityType.Grass5;
				case 6:
					return EntityType.Grass6;
				case 7:
					return EntityType.Grass7;
				default:
					throw new AssertionError();
			}
		}
		return null;
	}
}
