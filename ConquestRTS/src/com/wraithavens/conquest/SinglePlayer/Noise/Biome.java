package com.wraithavens.conquest.SinglePlayer.Noise;

public enum Biome{
	// Oceans biomes. Catch all, but only under sea-level. (Plus beach)
	// DeepWater(0, 100, 0, 100, 0, 35),
	// Ocean(0, 100, 0, 100, 35, 45),
	// ShallowOcean(0, 100, 0, 100, 45, 50),
	// Beach(0, 100, 0, 100, 50, 50.05f),
	// Other biomes.
	TayleaMeadow(0, 100, 50, 100, 2),
	ArcstoneHills(0, 100, 0, 50, 2),
	// And finally, a catch-all. This should never be hit.
	Void(0, 100, 0, 100, 0, 100, 0);
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
	private Biome(float minH, float maxH, float minT, float maxT, float minL, float maxL, int biomeType){
		this.minH = minH;
		this.maxH = maxH;
		this.minT = minT;
		this.maxT = maxT;
		this.minL = minL;
		this.maxL = maxL;
		this.biomeType = biomeType;
	}
	private Biome(int minH, int maxH, int minT, int maxT, float minL, float maxL, int biomeType){
		this(minH*0.01f, maxH*0.01f, minT*0.01f, maxT*0.01f, minL*0.01f, maxL*0.01f, biomeType);
	}
	private Biome(int minH, int maxH, int minT, int maxT, int biomeType){
		this(minH*0.01f, maxH*0.01f, minT*0.01f, maxT*0.01f, 0.0f, 1.0f, biomeType);
	}
	public float averageHumidity(){
		return (maxH+minH)/2;
	}
	public float averageTempature(){
		return (maxT+minT)/2;
	}
	public int getType(){
		return biomeType;
	}
	private boolean fitsIn(double h, double t, double l){
		return h>=minH&&h<=maxH&&t>=minT&&t<=maxT&&l>=minL&&l<=maxL;
	}
}
