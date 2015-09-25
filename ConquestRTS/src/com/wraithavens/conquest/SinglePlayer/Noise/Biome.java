package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.SinglePlayer.Particles.ParticleEngine;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes.PollenParticleEngine;
import com.wraithavens.conquest.SinglePlayer.Particles.ParticleTypes.ZipperParticleEngine;

public enum Biome{
	// Oceans biomes. Catch all, but only under sea-level. (Plus beach)
	// DeepWater(0, 100, 0, 100, 0, 35),
	// Ocean(0, 100, 0, 100, 35, 45),
	// ShallowOcean(0, 100, 0, 100, 45, 50),
	// Beach(0, 100, 0, 100, 50, 50.05f),
	// Other biomes.
	TayleaMeadow(0.0f, 1.0f, 0.5f, 1.0f, 0.0f, 1.0f, 2, PollenParticleEngine.class),
	ArcstoneHills(0.0f, 1.0f, 0.0f, 0.5f, 0.0f, 1.0f, 2, ZipperParticleEngine.class),
	AesiaFields(0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f, 2, null),
	// And finally, a catch-all. This should never be hit.
	Void(0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0, null);
	public static Biome getFittingBiome(double h, double t, double l){
		for(Biome b : values())
			if(b.fitsIn(h, t, l))
				return b;
		return null;
	}
	public static final int BiomeTypeNull = 0;
	public static final int BiomeTypeOcean = 1;
	public static final int BiomeTypeGrasslands = 2;
	private final float minH;
	private final float maxH;
	private final float minT;
	private final float maxT;
	private final float minL;
	private final float maxL;
	private final int biomeType;
	private final Class<? extends ParticleEngine> particleEngine;
	private Biome(
		float minH, float maxH, float minT, float maxT, float minL, float maxL, int biomeType,
		Class<? extends ParticleEngine> particleEngine){
		this.minH = minH;
		this.maxH = maxH;
		this.minT = minT;
		this.maxT = maxT;
		this.minL = minL;
		this.maxL = maxL;
		this.biomeType = biomeType;
		this.particleEngine = particleEngine;
	}
	public Class<? extends ParticleEngine> getParticleEngine(){
		return particleEngine;
	}
	private boolean fitsIn(double h, double t, double l){
		return h>=minH&&h<=maxH&&t>=minT&&t<=maxT&&l>=minL&&l<=maxL;
	}
	int getType(){
		return biomeType;
	}
}
