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
	TayleaMeadow(0.0f, 0.5f, 0.0f, 0.5f, 0.0f, 1.0f, PollenParticleEngine.class),
	ArcstoneHills(0.5f, 1.0f, 0.0f, 0.5f, 0.0f, 1.0f, ZipperParticleEngine.class),
	AesiaFields(0.0f, 1.0f, 0.5f, 1.0f, 0.0f, 1.0f, null),
	// And finally, a catch-all. This should never be hit.
	Void(0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, null);
	public static Biome getByName(String name){
		for(Biome biome : values())
			if(biome.name().equalsIgnoreCase(name))
				return biome;
		return null;
	}
	public static Biome getFittingBiome(double h, double t, double l){
		for(Biome b : values())
			if(b.fitsIn(h, t, l))
				return b;
		return null;
	}
	private final float minH;
	private final float maxH;
	private final float minT;
	private final float maxT;
	private final float minL;
	private final float maxL;
	private final Class<? extends ParticleEngine> particleEngine;
	private Biome(
		float minH, float maxH, float minT, float maxT, float minL, float maxL,
		Class<? extends ParticleEngine> particleEngine){
		this.minH = minH;
		this.maxH = maxH;
		this.minT = minT;
		this.maxT = maxT;
		this.minL = minL;
		this.maxL = maxL;
		this.particleEngine = particleEngine;
	}
	public Class<? extends ParticleEngine> getParticleEngine(){
		return particleEngine;
	}
	private boolean fitsIn(double h, double t, double l){
		return h>=minH&&h<=maxH&&t>=minT&&t<=maxT&&l>=minL&&l<=maxL;
	}
}
