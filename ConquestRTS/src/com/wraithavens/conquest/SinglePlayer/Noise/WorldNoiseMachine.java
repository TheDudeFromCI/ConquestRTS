package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.HashMap;
import com.wraithavens.conquest.Math.Vector3f;
import com.wraithavens.conquest.SinglePlayer.Blocks.BiomeNoise.ArcstoneHillsNoise;
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
				float a = h-(int)h;
				float b = t-(int)t;
				blend(p1, p2, a, t1);
				blend(p3, p4, a, t2);
				blend(t1, t2, b, t3);
				colorOut.set(t3[0], t3[1], t3[2]);
				return;
		}
	}
	private static void blend(float[] a, float[] b, float c, float[] out){
		c = (float)((1-Math.cos(c*Math.PI))/2);
		out[0] = a[0]*(1-c)+b[0]*c;
		out[1] = a[1]*(1-c)+b[1]*c;
		out[2] = a[2]*(1-c)+b[2]*c;
	}
	private static float blendCubic(float a, float b, float c, float d, float frac){
		double mu2 = frac*frac;
		double a0 = d-c-a+b;
		double a1 = a-b-a0;
		double a2 = c-a;
		double a3 = b;
		return (float)(a0*frac*mu2+a1*mu2+a2*frac+a3);
	}
	private static int cheapFloor(float x){
		return x<0?(int)x-1:(int)x;
	}
	private static float clamp(float a, float b, float c){
		return a<b?b:a>c?c:a;
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
	private final TayleaMeadowNoise tayleaMeadowNoise;
	private final ArcstoneHillsNoise arcstoneHillsNoise;
	private final long[] seeds;
	private final float[] tempHTL = new float[3];
	private WorldNoiseMachine(
		long[] seeds, AdvancedNoise worldHeight, AdvancedNoise humidity, AdvancedNoise tempature){
		this.seeds = seeds;
		this.worldHeight = worldHeight;
		this.humidity = humidity;
		this.tempature = tempature;
		tayleaMeadowNoise = new TayleaMeadowNoise(seeds[3]);
		arcstoneHillsNoise = new ArcstoneHillsNoise(seeds[4]);
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
		getBiomeAt(x, z, tempHTL);
		return scaleHeight(tempHTL[0], tempHTL[1], tempHTL[2], x, z);
	}
	public float getHumidityRaw(float x, float z){
		return humidity.noise(x, z);
	}
	public float getLevelRaw(float x, float y){
		return worldHeight.noise(x, y)/worldHeight.getMaxHeight();
	}
	public float getTempatureRaw(float x, float z){
		return tempature.noise(x, z);
	}
	public int scaleHeight(float h, float t, float l, float x, float z){
		final float mapSize = 50;
		h *= mapSize;
		t *= mapSize;
		Biome[] c = new Biome[16];
		c[0] = Biome.getFittingBiome(clamp((int)(h-1)/mapSize, 0, 1), clamp((int)(t-1)/mapSize, 0, 1), l);
		c[1] = Biome.getFittingBiome(clamp((int)h/mapSize, 0, 1), clamp((int)(t-1)/mapSize, 0, 1), l);
		c[2] = Biome.getFittingBiome(clamp((int)(h+1)/mapSize, 0, 1), clamp((int)(t-1)/mapSize, 0, 1), l);
		c[3] = Biome.getFittingBiome(clamp((int)(h+2)/mapSize, 0, 1), clamp((int)(t-1)/mapSize, 0, 1), l);
		c[4] = Biome.getFittingBiome(clamp((int)(h-1)/mapSize, 0, 1), clamp((int)t/mapSize, 0, 1), l);
		c[5] = Biome.getFittingBiome(clamp((int)h/mapSize, 0, 1), clamp((int)t/mapSize, 0, 1), l);
		c[6] = Biome.getFittingBiome(clamp((int)(h+1)/mapSize, 0, 1), clamp((int)t/mapSize, 0, 1), l);
		c[7] = Biome.getFittingBiome(clamp((int)(h+2)/mapSize, 0, 1), clamp((int)t/mapSize, 0, 1), l);
		c[8] = Biome.getFittingBiome(clamp((int)(h-1)/mapSize, 0, 1), clamp((int)(t+1)/mapSize, 0, 1), l);
		c[9] = Biome.getFittingBiome(clamp((int)h/mapSize, 0, 1), clamp((int)(t+1)/mapSize, 0, 1), l);
		c[10] = Biome.getFittingBiome(clamp((int)(h+1)/mapSize, 0, 1), clamp((int)(t+1)/mapSize, 0, 1), l);
		c[11] = Biome.getFittingBiome(clamp((int)(h+2)/mapSize, 0, 1), clamp((int)(t+1)/mapSize, 0, 1), l);
		c[12] = Biome.getFittingBiome(clamp((int)(h-1)/mapSize, 0, 1), clamp((int)(t+2)/mapSize, 0, 1), l);
		c[13] = Biome.getFittingBiome(clamp((int)h/mapSize, 0, 1), clamp((int)(t+2)/mapSize, 0, 1), l);
		c[14] = Biome.getFittingBiome(clamp((int)(h+1)/mapSize, 0, 1), clamp((int)(t+2)/mapSize, 0, 1), l);
		c[15] = Biome.getFittingBiome(clamp((int)(h+2)/mapSize, 0, 1), clamp((int)(t+2)/mapSize, 0, 1), l);
		HashMap<Biome,Float> biomeHeights = new HashMap();
		for(Biome biome : c)
			if(!biomeHeights.containsKey(biome))
				biomeHeights.put(biome, getBiomeTempHeight(biome, x, z));
		float[] f = new float[16];
		f[0] = biomeHeights.get(c[0]);
		f[1] = biomeHeights.get(c[1]);
		f[2] = biomeHeights.get(c[2]);
		f[3] = biomeHeights.get(c[3]);
		f[4] = biomeHeights.get(c[4]);
		f[5] = biomeHeights.get(c[5]);
		f[6] = biomeHeights.get(c[6]);
		f[7] = biomeHeights.get(c[7]);
		f[8] = biomeHeights.get(c[8]);
		f[9] = biomeHeights.get(c[9]);
		f[10] = biomeHeights.get(c[10]);
		f[11] = biomeHeights.get(c[11]);
		f[12] = biomeHeights.get(c[12]);
		f[13] = biomeHeights.get(c[13]);
		f[14] = biomeHeights.get(c[14]);
		f[15] = biomeHeights.get(c[15]);
		float a = h-(int)h;
		float t1 = blendCubic(f[0], f[1], f[2], f[3], a);
		float t2 = blendCubic(f[4], f[5], f[6], f[7], a);
		float t3 = blendCubic(f[8], f[9], f[10], f[11], a);
		float t4 = blendCubic(f[12], f[13], f[14], f[15], a);
		l -= 0.5f;
		l *= blendCubic(t1, t2, t3, t4, t-(int)t);
		return cheapFloor(l);
	}
	private float getBiomeTempHeight(Biome biome, float x, float z){
		switch(biome){
			case TayleaMeadow:
				return tayleaMeadowNoise.getHeight(x, z);
			case ArcstoneHills:
				return arcstoneHillsNoise.getHeight(x, z);
			default:
				throw new AssertionError();
		}
	}
}
