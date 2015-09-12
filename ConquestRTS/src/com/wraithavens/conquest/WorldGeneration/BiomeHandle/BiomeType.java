package com.wraithavens.conquest.WorldGeneration.BiomeHandle;

public enum BiomeType{
	Ocean(0.5f, 1.0f, 0.0f, 1.0f),
	TayleaMeadow(0.0f, 0.5f, 0.0f, 1.0f);
	public static BiomeType getFittingType(float h, float t){
		for(BiomeType type : values())
			if(h>=type.minH&&h<=type.maxH&&t>=type.minT&&t<=type.maxT)
				return type;
		return null;
	}
	private final float minH;
	private final float maxH;
	private final float minT;
	private final float maxT;
	private BiomeType(float minH, float maxH, float minT, float maxT){
		this.minH = minH;
		this.maxH = maxH;
		this.minT = minT;
		this.maxT = maxT;
	}
}
