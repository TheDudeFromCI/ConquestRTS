package com.wraithavens.conquest.WorldGeneration.BiomeHandle;

import java.util.Random;
import com.wraithavens.conquest.Utility.InterpolationFunction;

class NoiseLayer{
	private static final long s = 4294967291L;
	private final long seed;
	private final float smoothness;
	private final float weight;
	private final InterpolationFunction function;
	private final float[] fracs, v;
	private final float[][] compressions;
	private final int[] reals, edge, c;
	private final Random r = new Random();
	NoiseLayer(long seed, float smoothness, float weight, int dimensions, InterpolationFunction function){
		this.seed = seed;
		this.smoothness = smoothness;
		this.weight = weight;
		this.function = function;
		reals = new int[dimensions];
		fracs = new float[dimensions];
		v = new float[(int)Math.pow(2, dimensions)];
		edge = new int[dimensions+1];
		c = new int[dimensions];
		for(int i = 0; i<c.length; i++)
			c[i] = 1<<i;
		compressions = new float[dimensions][];
		for(int i = 0; i<compressions.length; i++)
			compressions[i] = new float[1<<compressions.length-1-i];
	}
	private float compress(float[] v, float[] fracs, int stage){
		if(v.length==2)
			return function.interpolate(v[0], v[1], fracs[stage]);
		for(int i = 0; i<compressions[stage].length; i++)
			compressions[stage][i] = function.interpolate(v[i*2], v[i*2+1], fracs[stage]);
		return compress(compressions[stage], fracs, stage+1);
	}
	float getWeight(){
		return weight;
	}
	float noise(float... x){
		float a;
		int i, j;
		long t;
		for(i = 0; i<x.length; i++){
			a = x[i]/smoothness;
			reals[i] = a<0?(int)a-1:(int)a;
			fracs[i] = a-reals[i];
		}
		for(i = 0; i<v.length; i++){
			for(j = 0; j<x.length; j++)
				edge[j] = (i&c[j])==c[j]?reals[j]+1:reals[j];
			t = seed;
			for(j = 0; j<edge.length; j++)
				t = t*s+edge[j];
			for(j = 0; j<edge.length; j++)
				t += edge[j]*edge[j]+s;
			r.setSeed(t);
			v[i] = r.nextFloat();
		}
		return compress(v, fracs, 0)*weight;
	}
}
