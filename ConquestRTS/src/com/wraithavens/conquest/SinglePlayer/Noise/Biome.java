package com.wraithavens.conquest.SinglePlayer.Noise;

public enum Biome{
	// Oceans biomes. Catch all, but only under sea-level. (Plus beach)
	// DeepWater(0, 100, 0, 100, 0, 35),
	// Ocean(0, 100, 0, 100, 35, 45),
	// ShallowOcean(0, 100, 0, 100, 45, 50),
	// Beach(0, 100, 0, 100, 50, 50.05f),
	// Other biomes.
	TayleaMeadow(0, 100, 50, 100),
	ArcstoneHills(0, 100, 0, 50),
	// And finally, a catch-all. This should never be hit.
	Void(0, 100, 0, 100, 0, 100);
	public static Biome getFittingBiome(float h, float t, float l){
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
	private Biome(float minH, float maxH, float minT, float maxT, float minL, float maxL){
		this.minH = minH;
		this.maxH = maxH;
		this.minT = minT;
		this.maxT = maxT;
		this.minL = minL;
		this.maxL = maxL;
	}
	private Biome(int minH, int maxH, int minT, int maxT){
		this(minH*0.01f, maxH*0.01f, minT*0.01f, maxT*0.01f, 0.0f, 1.0f);
	}
	private Biome(int minH, int maxH, int minT, int maxT, float minL, float maxL){
		this(minH*0.01f, maxH*0.01f, minT*0.01f, maxT*0.01f, minL*0.01f, maxL*0.01f);
	}
	public float averageHumidity(){
		return (maxH+minH)/2;
	}
	public float averageTempature(){
		return (maxT+minT)/2;
	}
	private boolean fitsIn(float h, float t, float l){
		return h>=minH&&h<=maxH&&t>=minT&&t<=maxT&&l>=minL&&l<=maxL;
	}
}
