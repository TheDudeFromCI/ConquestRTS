package com.wraithavens.conquest.SinglePlayer.Noise;

import java.util.ArrayList;
import java.util.Random;

public class PointGenerator2D{
	private static int floor(float x){
		return x>=0?(int)x:(int)x-1;
	}
	private static final long s = 4294967291L;
	private final long seed;
	private final float averageDistanceApart;
	private final float maxShift;
	private final float consistancy;
	private Random r = new Random();
	public PointGenerator2D(long seed, float averageDistanceApart, float minDistanceApart, float consistancy){
		this.seed = seed;
		this.averageDistanceApart = averageDistanceApart;
		this.consistancy = consistancy;
		maxShift = minDistanceApart==0?0:minDistanceApart/averageDistanceApart;
	}
	public void noise(float x, float y, float size, ArrayList<float[]> points){
		int startX = floor(x/averageDistanceApart)-1;
		int startY = floor(y/averageDistanceApart)-1;
		int endX = floor(x/averageDistanceApart+size/averageDistanceApart)+1;
		int endY = floor(y/averageDistanceApart+size/averageDistanceApart)+1;
		int a, b;
		float tempX, tempZ;
		for(a = startX; a<=endX; a++)
			for(b = startY; b<=endY; b++){
				if(random(a, b, 0)>=consistancy)
					continue;
				tempX = (a+(random(a, b, 1)*2-1)*maxShift)*averageDistanceApart;
				tempZ = (b+(random(a, b, 2)*2-1)*maxShift)*averageDistanceApart;
				if(tempX>=x&&tempZ>=y&&tempX<x+size&&tempZ<y+size)
					points.add(new float[]{
						tempX, tempZ, (float)(random(a, b, 3)*Math.PI*2), random(a, b, 4)*0.2f+0.9f
					});
			}
	}
	private float random(int x, int y, int z){
		long t = seed;
		t = t*s+x;
		t = t*s+y;
		t = t*s+z;
		t += x*x+s;
		t += y*y+s;
		t += z*z+s;
		r.setSeed(t);
		return r.nextFloat();
	}
}
