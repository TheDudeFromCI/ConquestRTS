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
		maxShift = minDistanceApart/averageDistanceApart;
	}
	public void noise(float x, float y, float size, ArrayList<float[]> points){
		x /= averageDistanceApart;
		y /= averageDistanceApart;
		size /= averageDistanceApart;
		int startX = floor(x)-1;
		int startY = floor(y)-1;
		int endX = floor(x+size)+1;
		int endY = floor(y+size)+1;
		int a, b;
		for(a = startX; a<=endX; a++)
			for(b = startY; b<=endY; b++){
				if(random(a, b, 0)>=consistancy)
					continue;
				points.add(new float[]{
					a+(random(a, b, 1)*2-1)*maxShift, b+(random(a, b, 2)*2-1)*maxShift
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
