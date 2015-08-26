package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.Random;
import com.wraithavens.conquest.Utility.InterpolationFunction;

public class NoiseGenerator{
	private static float findMaxHeight(int detail){
		float m = 0;
		for(int i = 0; i<detail; i++)
			m += Math.pow(2, -i);
		return m;
	}
	private static final long s = 4294967291L;
	private final int detail;
	private float[] fracs, v;
	private float[][] compressions;
	private InterpolationFunction function;
	private final float maxHeight;
	private int[] reals, edge, c;
	private int[] layerSeeds;
	private final long seed;
	private final float smoothness;
	private Random r = new Random();
	public NoiseGenerator(long seed, float smoothness, int detail){
		this.seed = seed;
		this.smoothness = smoothness;
		this.detail = detail+1;
		maxHeight = findMaxHeight(detail);
		layerSeeds = new int[this.detail];
		Random r = new Random();
		for(int i = 0; i<layerSeeds.length; i++){
			r.setSeed(seed+i);
			layerSeeds[i] = r.nextInt();
		}
	}
	public float noise(float... x){
		if(reals==null||x.length!=reals.length){
			reals = new int[x.length];
			fracs = new float[x.length];
			v = new float[(int)Math.pow(2, x.length)];
			edge = new int[x.length+1];
			c = new int[x.length];
			for(int i = 0; i<c.length; i++)
				c[i] = 1<<i;
			compressions = new float[x.length][];
			for(int i = 0; i<compressions.length; i++)
				compressions[i] = new float[1<<compressions.length-1-i];
		}
		float total = 0;
		int pow;
		float a;
		int i, j;
		long t;
		for(int k = 0; k<detail; k++){
			pow = 1<<k;
			edge[x.length] = layerSeeds[k];
			for(i = 0; i<x.length; i++){
				a = x[i]/smoothness*pow;
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
			total += compress(v, fracs, 0)/pow;
		}
		return Math.max(Math.min(total/maxHeight, 1), 0);
	}
	public void setFunction(InterpolationFunction function){
		this.function = function;
	}
	private float compress(float[] v, float[] fracs, int stage){
		if(v.length==2)
			return function.interpolate(v[0], v[1], fracs[stage]);
		for(int i = 0; i<compressions[stage].length; i++)
			compressions[stage][i] = function.interpolate(v[i*2], v[i*2+1], fracs[stage]);
		return compress(compressions[stage], fracs, stage+1);
	}
}
