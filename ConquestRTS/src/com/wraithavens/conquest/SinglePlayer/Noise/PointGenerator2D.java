package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.ArrayList;
import java.util.Random;

public class PointGenerator2D{
	public static PointGenerator2D build(long seed, float averageDistance, float unitSize){
		int passes = Math.round(unitSize/averageDistance);
		float rate = unitSize/passes/averageDistance;
		passes *= 2;
		rate /= 2;
		float seperation = 0.5f;
		return new PointGenerator2D(seed, rate, passes, unitSize, seperation);
	}
	private static final long s = 4294967291L;
	private final float rate;
	private final int passes;
	private final float unitSize;
	private final long seed;
	private final float seperation;
	private Random r = new Random();
	public PointGenerator2D(long seed, float rate, int passes, float unitSize, float seperation){
		this.seed = seed;
		this.rate = rate;
		this.passes = passes;
		this.unitSize = unitSize;
		this.seperation = 1-seperation;
	}
	public void noise(int x, int y, ArrayList<float[]> points){
		{
			long t = seed;
			t = t*s+x;
			t = t*s+y;
			t += x*x+s;
			t += y*y+s;
			r.setSeed(t);
		}
		int a, b;
		float size = unitSize/passes;
		for(a = 0; a<passes; a++)
			for(b = 0; b<passes; b++)
				if(r.nextFloat()<rate)
					points.add(new float[]{
						a*size+x+(r.nextFloat()-0.5f)*size*seperation,
						b*size+y+(r.nextFloat()-0.5f)*size*seperation
					});
	}
}
