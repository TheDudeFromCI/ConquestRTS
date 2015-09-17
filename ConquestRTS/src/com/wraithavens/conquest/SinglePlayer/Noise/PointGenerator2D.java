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
		maxShift = (minDistanceApart==0?0:minDistanceApart/averageDistanceApart)*0.5f;
	}
	public void noise(float x, float y, float size, ArrayList<float[]> points){
		float subX = x/averageDistanceApart;
		float subY = y/averageDistanceApart;
		float subSize = size/averageDistanceApart;
		int startX = floor(subX)-1;
		int startY = floor(subY)-1;
		int endX = floor(subX+subSize)+1;
		int endY = floor(subY+subSize)+1;
		int a, b;
		float tempX, tempZ;
		for(a = startX; a<=endX; a++)
			for(b = startY; b<=endY; b++){
				if(random(a, b)>=consistancy)
					continue;
				tempX = (a+(r.nextFloat()*2-1)*maxShift)*averageDistanceApart;
				tempZ = (b+(r.nextFloat()*2-1)*maxShift)*averageDistanceApart;
				if(tempX>=x&&tempZ>=y&&tempX<x+size&&tempZ<y+size)
					points.add(new float[]{
						tempX, tempZ, (float)(r.nextFloat()*Math.PI*2), r.nextFloat()*0.2f+0.9f
					});
			}
	}
	private float random(int x, int y){
		long t = seed;
		t = t*s+x;
		t = t*s+y;
		t += x*x+s;
		t += y*y+s;
		r.setSeed(t);
		return r.nextFloat();
	}
}
