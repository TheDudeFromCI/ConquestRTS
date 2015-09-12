package com.wraithavens.conquest.WorldGeneration.BiomeHandle;

public class NoiseMachine{
	private final NoiseLayer[] layers;
	private final float totalWeight;
	NoiseMachine(NoiseLayer[] layers){
		this.layers = layers;
		float w = 0;
		for(int i = 0; i<layers.length; i++)
			w += layers[i].getWeight();
		totalWeight = w;
	}
	float noise(float... x){
		float t = 0;
		for(int i = 0; i<layers.length; i++)
			t += layers[i].noise(x);
		return t/totalWeight;
	}
}
