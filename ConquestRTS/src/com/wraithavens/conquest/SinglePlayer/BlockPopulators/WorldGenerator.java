package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.util.Random;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.NoiseGenerator;

public class WorldGenerator{
	private final NoiseGenerator generator1;
	private final NoiseGenerator generator2;
	private final NoiseGenerator generator3;
	private final NoiseGenerator generator4;
	public WorldGenerator(){
		this((long)(Math.random()*Long.MAX_VALUE*(Math.random()>0.5?1:-1)));
	}
	private WorldGenerator(long seed){
		Random r = new Random(seed);
		generator1 = new NoiseGenerator(r.nextLong(), 100, 3);
		generator1.setFunction(new CosineInterpolation());
		generator2 = new NoiseGenerator(r.nextLong(), 100, 2);
		generator3 = new NoiseGenerator(r.nextLong(), 150, 3);
		generator4 = new NoiseGenerator(r.nextLong(), 200, 5);
		generator4.setFunction(new CosineInterpolation());
	}
	public int getHeightAt(float x, float z){
		float smoothing = generator4.noise(x, z)*20;
		x /= smoothing;
		z /= smoothing;
		int h = (int)(generator1.noise(x, z)*200);
		// int max = (int)(generator2.noise(x, z)*50+150);
		// int min = (int)(generator3.noise(x, z)*50);
		// if(h>max)h = max;
		// if(h<min)h = min;
		return h+1;
	}
}
