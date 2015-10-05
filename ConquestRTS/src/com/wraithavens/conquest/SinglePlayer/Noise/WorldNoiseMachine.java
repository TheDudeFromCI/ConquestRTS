package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise.AesiaFieldsNoise;
import com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise.ArcstoneHillsNoise;
import com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise.OceanNoise;
import com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise.TayleaMeadowNoise;
import com.wraithavens.conquest.Utility.CosineInterpolation;

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
		// ---
		// This will be the main control over how the world is generated. Based
		// on the seeds given, of course.
		// ---
		CosineInterpolation cos = new CosineInterpolation();
		SubNoise worldHeightNoise1 = SubNoise.build(seeds[0], 100000, 8, cos, 1, 0);
		SubNoise humidityNoise = SubNoise.build(seeds[1], 25000, 4, cos, 1, 0);
		SubNoise tempatureNoise = SubNoise.build(seeds[2], 25000, 4, cos, 1, 0);
		// ---
		// And compiling these together.
		// ---
		AdvancedNoise worldHeight = new AdvancedNoise();
		worldHeight.addSubNoise(worldHeightNoise1);
		AdvancedNoise humidity = new AdvancedNoise();
		humidity.addSubNoise(humidityNoise);
		AdvancedNoise tempature = new AdvancedNoise();
		tempature.addSubNoise(tempatureNoise);
		return new WorldNoiseMachine(seeds, worldHeight, humidity, tempature);
	}
	public static void getBiomeColorAt(float h, float t, Vector3f colorOut){
		// TODO Make this more effiecient.
		final float mapSize = BiomeTransitionSize;
		h *= mapSize;
		t *= mapSize;
		Biome c1 = Biome.getFittingBiome((int)h/mapSize, (int)t/mapSize, 1.0f);
		Biome c2 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)t/mapSize, 1.0f);
		Biome c3 = Biome.getFittingBiome((int)h/mapSize, (int)(t+1)/mapSize, 1.0f);
		Biome c4 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)(t+1)/mapSize, 1.0f);
		if(c1==c2&&c2==c3&&c3==c4){
			float[] temp = new float[3];
			getTempBiomeColorAt(c1, temp);
			colorOut.set(temp[0], temp[1], temp[2]);
			return;
		}
		float[] p1 = new float[3];
		float[] p2 = new float[3];
		float[] p3 = new float[3];
		float[] p4 = new float[3];
		getTempBiomeColorAt(c1, p1);
		getTempBiomeColorAt(c2, p2);
		getTempBiomeColorAt(c3, p3);
		getTempBiomeColorAt(c4, p4);
		float[] t1 = new float[3];
		float[] t2 = new float[3];
		float[] t3 = new float[3];
		float a = h-(int)h;
		float b = t-(int)t;
		blend(p1, p2, a, t1);
		blend(p3, p4, a, t2);
		blend(t1, t2, b, t3);
		colorOut.set(t3[0], t3[1], t3[2]);
	}
	private static float blend(float a, float b, float frac){
		frac = (float)((1-Math.cos(frac*Math.PI))/2);
		return a*(1-frac)+b*frac;
	}
	private static void blend(float[] a, float[] b, float c, float[] out){
		c = (float)((1-Math.cos(c*Math.PI))/2);
		out[0] = a[0]*(1-c)+b[0]*c;
		out[1] = a[1]*(1-c)+b[1]*c;
		out[2] = a[2]*(1-c)+b[2]*c;
	}
	private static int cheapFloor(float x){
		return x<0?(int)x-1:(int)x;
	}
	private static void getTempBiomeColorAt(Biome biome, float[] out){
		switch(biome){
			case TayleaMeadow:
				out[0] = 109/255f;
				out[1] = 135/255f;
				out[2] = 24/255f;
				return;
			case ArcstoneHills:
				out[0] = 90/255f;
				out[1] = 110/255f;
				out[2] = 20/255f;
				return;
			case AesiaFields:
				out[0] = 93/255f;
				out[1] = 115/255f;
				out[2] = 19/255f;
				return;
			case Ocean:
				out[0] = 0/255f;
				out[1] = 255/255f;
				out[2] = 100/255f;
				return;
			default:
				throw new RuntimeException();
		}
	}
	public static final int BiomeTransitionSize = 300;
	private final AdvancedNoise worldHeight;
	private final AdvancedNoise tempature;
	private final AdvancedNoise humidity;
	private final TayleaMeadowNoise tayleaMeadowNoise;
	private final ArcstoneHillsNoise arcstoneHillsNoise;
	private final AesiaFieldsNoise aesiaFieldsNoise;
	private final OceanNoise oceanNoise;
	private final long[] seeds;
	private final float[] tempHTL = new float[3];
	/**
	 * Seed Format: <br>
	 * 0 = Base World Height <br>
	 * 1 = Humidity <br>
	 * 2 = Tempature <br>
	 * 3 = Giant Entity Seed <br>
	 * 4-5 = Biome specific noise.
	 */
	private WorldNoiseMachine(
		long[] seeds, AdvancedNoise worldHeight, AdvancedNoise humidity, AdvancedNoise tempature){
		this.seeds = seeds;
		this.worldHeight = worldHeight;
		this.humidity = humidity;
		this.tempature = tempature;
		tayleaMeadowNoise = new TayleaMeadowNoise(seeds[4]);
		arcstoneHillsNoise = new ArcstoneHillsNoise(seeds[5]);
		aesiaFieldsNoise = new AesiaFieldsNoise(seeds[6]);
		oceanNoise = new OceanNoise(seeds[7]);
	}
	public Biome getBiomeAt(int x, int z, float[] heightOut){
		heightOut[0] = getHumidityRaw(x, z);
		heightOut[1] = getTempatureRaw(x, z);
		heightOut[2] = getLevelRaw(x, z);
		return Biome.getFittingBiome(heightOut[0], heightOut[1], heightOut[2]);
	}
	public long getGiantEntitySeed(){
		return seeds[3];
	}
	public int getGroundLevel(float x, float z){
		return getGroundLevel(cheapFloor(x), cheapFloor(z));
	}
	public synchronized int getGroundLevel(int x, int z){
		getBiomeAt(x, z, tempHTL);
		return scaleHeight(tempHTL[0], tempHTL[1], tempHTL[2], x, z);
	}
	public int scaleHeight(float h, float t, float l, int x, int z){
		final float mapSize = 50;
		h *= mapSize;
		t *= mapSize;
		l *= mapSize;
		Biome c1 = Biome.getFittingBiome((int)h/mapSize, (int)t/mapSize, (int)l/mapSize);
		Biome c2 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)t/mapSize, (int)l/mapSize);
		Biome c3 = Biome.getFittingBiome((int)h/mapSize, (int)(t+1)/mapSize, (int)l/mapSize);
		Biome c4 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)(t+1)/mapSize, (int)l/mapSize);
		Biome c5 = Biome.getFittingBiome((int)h/mapSize, (int)t/mapSize, (int)(l+1)/mapSize);
		Biome c6 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)t/mapSize, (int)(l+1)/mapSize);
		Biome c7 = Biome.getFittingBiome((int)h/mapSize, (int)(t+1)/mapSize, (int)(l+1)/mapSize);
		Biome c8 = Biome.getFittingBiome((int)(h+1)/mapSize, (int)(t+1)/mapSize, (int)(l+1)/mapSize);
		if(c1==c2&&c2==c3&&c3==c4&&c4==c5&&c5==c6&&c6==c7&&c7==c8)
			return cheapFloor(getBiomeTempHeight(c1, x, z));
		float a = h-(int)h;
		float b = t-(int)t;
		float c = l-(int)l;
		return cheapFloor(blend(
			blend(blend(getBiomeTempHeight(c1, x, z), getBiomeTempHeight(c2, x, z), a),
				blend(getBiomeTempHeight(c3, x, z), getBiomeTempHeight(c4, x, z), a), b),
				blend(blend(getBiomeTempHeight(c5, x, z), getBiomeTempHeight(c6, x, z), a),
					blend(getBiomeTempHeight(c7, x, z), getBiomeTempHeight(c8, x, z), a), b), c));
	}
	private float getBiomeTempHeight(Biome biome, float x, float z){
		switch(biome){
			case TayleaMeadow:
				return tayleaMeadowNoise.getHeight(x, z);
			case ArcstoneHills:
				return arcstoneHillsNoise.getHeight(x, z);
			case AesiaFields:
				return aesiaFieldsNoise.getHeight(x, z);
			case Ocean:
				return oceanNoise.getHeight(x, z);
			default:
				throw new RuntimeException();
		}
	}
	private float getHumidityRaw(float x, float z){
		return humidity.noise(x, z);
	}
	private float getLevelRaw(float x, float y){
		return worldHeight.noise(x, y)/worldHeight.getMaxHeight();
	}
	private float getTempatureRaw(float x, float z){
		return tempature.noise(x, z);
	}
}
