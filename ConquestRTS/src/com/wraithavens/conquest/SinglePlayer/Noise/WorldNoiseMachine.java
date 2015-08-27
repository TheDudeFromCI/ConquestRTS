package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.GlError;
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
		GlError.out("Making world noise machine.");
		// ---
		// This will be the main control over how the world is generated. Based
		// on the seeds given, of course.
		// ---
		CosineInterpolation cos = new CosineInterpolation();
		SubNoise worldHeightNoise1 = SubNoise.build(seeds[0], 10000, 8, cos, 1500, 0);
		SubNoise humidityNoise = SubNoise.build(seeds[1], 5000, 4, cos, 1, 0);
		SubNoise tempatureNoise = SubNoise.build(seeds[2], 5000, 4, cos, 1, 0);
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
	public static void getBiomeColorAt(Biome biome, float h, float t, Vector3f colorOut){
		switch(biome.getType()){
			case Biome.BiomeTypeNull:
				throw new AssertionError();
			case Biome.BiomeTypeOcean:
				// ---
				// TODO
				// ---
				return;
			case Biome.BiomeTypeGrasslands:
				final float mapSize = 100;
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
				float x = h-(int)h;
				float y = t-(int)t;
				blend(p1, p2, x, t1);
				blend(p3, p4, x, t2);
				blend(t1, t2, y, t3);
				colorOut.set(t3[0], t3[1], t3[2]);
				return;
		}
	}
	@SuppressWarnings("unused")
	public static int scaleHeight(Biome biome, float h, float t, float l){
		float a;
		// ---
		// TODO Make biome heights blend together better.
		// ---
		// switch(biome){
		// case TayleaMeadow:
		// a = height*2000;
		// break;
		// case ArcstoneHills:
		// a = height*4000;
		// break;
		// default:
		// a = 0;
		// break;
		// }
		a = (l-0.5f)*2000;
		return a<0?(int)a-1:(int)a;
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
			default:
				return;
		}
	}
	private final AdvancedNoise worldHeight;
	private final AdvancedNoise tempature;
	private final AdvancedNoise humidity;
	private final long[] seeds;
	private final float[] tempHTL = new float[3];
	private WorldNoiseMachine(
		long[] seeds, AdvancedNoise worldHeight, AdvancedNoise humidity, AdvancedNoise tempature){
		this.seeds = seeds;
		this.worldHeight = worldHeight;
		this.humidity = humidity;
		this.tempature = tempature;
	}
	public WorldNoiseMachine createInstance(){
		return generate(seeds);
	}
	public Biome getBiomeAt(int x, int z, float[] heightOut){
		heightOut[0] = getHumidityRaw(x, z);
		heightOut[1] = getTempatureRaw(x, z);
		heightOut[2] = getLevelRaw(x, z);
		return Biome.getFittingBiome(heightOut[0], heightOut[1], heightOut[2]);
	}
	public int getGroundLevel(float x, float z){
		return getGroundLevel(cheapFloor(x), cheapFloor(z));
	}
	public synchronized int getGroundLevel(int x, int z){
		Biome biome = getBiomeAt(x, z, tempHTL);
		return scaleHeight(biome, tempHTL[0], tempHTL[1], tempHTL[2]);
	}
	public synchronized float getHumidityRaw(float x, float z){
		return humidity.noise(x, z);
	}
	public synchronized float getLevelRaw(float x, float y){
		return worldHeight.noise(x, y)/worldHeight.getMaxHeight();
	}
	public synchronized float getTempatureRaw(float x, float z){
		return tempature.noise(x, z);
	}
}
