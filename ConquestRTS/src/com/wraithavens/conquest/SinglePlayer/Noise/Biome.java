package com.wraithavens.conquest.SinglePlayer.Noise;


public enum Biome{
	// Oceans biomes. Catch all, but only under sea-level. (Plus beach)
	DeepWater(0, 100, 0, 100, 0, 35),
	Ocean(0, 100, 0, 100, 35, 45),
	ShallowOcean(0, 100, 0, 100, 45, 50),
	Beach(0, 100, 0, 100, 50, 50.05f),
	// These need to be checked out of order, due to overlapping boxes.
	Swamp(60, 80, 60, 76),
	Mountains(15, 30, 50, 70),
	ExtremeDesert(0, 5, 80, 100),
	Jungle(95, 100, 80, 100),
	// Other biomes.
	Polar(0, 100, 0, 5),
	Tundra(0, 100, 5, 15),
	Tiaga(0, 100, 15, 30),
	ColdDesert(0, 40, 30, 60),
	Prairie(40, 60, 30, 60),
	TemprateForest(60, 100, 30, 60),
	DryDesert(0, 25, 60, 100),
	TropicalGrasslands(25, 45, 60, 100),
	Savannha(45, 55, 60, 100),
	TropicalForest(55, 75, 60, 100),
	Rainforest(75, 100, 60, 100),
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
