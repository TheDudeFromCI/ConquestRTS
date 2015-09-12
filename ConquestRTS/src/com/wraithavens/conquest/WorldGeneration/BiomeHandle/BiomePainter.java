package com.wraithavens.conquest.WorldGeneration.BiomeHandle;

import java.util.Random;
import com.wraithavens.conquest.Utility.CosineInterpolation;
import com.wraithavens.conquest.Utility.LinearInterpolation;

public class BiomePainter{
	private static int cheapFloor(float x){
		return x>=0?(int)x:(int)x-1;
	}
	private NoiseMachine worldHeight;
	private NoiseMachine humidity;
	private NoiseMachine tempature;
	public int getHeight(int x, int z){
		return cheapFloor(worldHeight.noise(cheapFloor(x), cheapFloor(z)));
	}
	public void getHT(int x, int z, float[] out){
		out[0] = humidity.noise(x, z);
		out[1] = tempature.noise(x, z);
	}
	public void setSeeds(long seed1, long seed2, long seed3){
		Random r1 = new Random(seed1);
		Random r2 = new Random(seed2);
		Random r3 = new Random(seed3);
		LinearInterpolation lerp = new LinearInterpolation();
		CosineInterpolation cos = new CosineInterpolation();
		worldHeight =
			new NoiseMachine(new NoiseLayer[]{
				new NoiseLayer(r1.nextLong(), 30000, 1, 2, cos),
				new NoiseLayer(r1.nextLong(), 20000, 1, 2, cos),
				new NoiseLayer(r1.nextLong(), 10000, 1, 2, lerp),
				new NoiseLayer(r1.nextLong(), 5000, 1, 2, lerp),
				new NoiseLayer(r1.nextLong(), 3000, 1, 2, lerp), new NoiseLayer(r1.nextLong(), 2000, 1, 2, cos),
				new NoiseLayer(r1.nextLong(), 1000, 1, 2, cos), new NoiseLayer(r1.nextLong(), 500, 1, 2, lerp),
				new NoiseLayer(r1.nextLong(), 300, 1, 2, lerp), new NoiseLayer(r1.nextLong(), 200, 1, 2, cos),
				new NoiseLayer(r1.nextLong(), 50, 1, 2, cos), new NoiseLayer(r1.nextLong(), 20, 1, 2, lerp),
				new NoiseLayer(r1.nextLong(), 5, 1, 2, lerp), new NoiseLayer(r1.nextLong(), 1, 1, 2, lerp)
			});
		humidity =
			new NoiseMachine(new NoiseLayer[]{
				new NoiseLayer(r2.nextLong(), 30000, 1, 2, cos),
				new NoiseLayer(r2.nextLong(), 20000, 1, 2, cos),
				new NoiseLayer(r2.nextLong(), 10000, 1, 2, lerp),
				new NoiseLayer(r2.nextLong(), 5000, 1, 2, lerp),
				new NoiseLayer(r2.nextLong(), 3000, 1, 2, lerp), new NoiseLayer(r2.nextLong(), 2000, 1, 2, cos),
				new NoiseLayer(r2.nextLong(), 1000, 1, 2, cos), new NoiseLayer(r2.nextLong(), 500, 1, 2, lerp),
				new NoiseLayer(r2.nextLong(), 300, 1, 2, lerp), new NoiseLayer(r2.nextLong(), 200, 1, 2, cos),
				new NoiseLayer(r2.nextLong(), 50, 1, 2, cos), new NoiseLayer(r2.nextLong(), 20, 1, 2, lerp),
				new NoiseLayer(r2.nextLong(), 5, 1, 2, lerp), new NoiseLayer(r2.nextLong(), 1, 1, 2, lerp)
			});
		tempature =
			new NoiseMachine(new NoiseLayer[]{
				new NoiseLayer(r3.nextLong(), 30000, 1, 2, cos),
				new NoiseLayer(r3.nextLong(), 20000, 1, 2, cos),
				new NoiseLayer(r3.nextLong(), 10000, 1, 2, lerp),
				new NoiseLayer(r3.nextLong(), 5000, 1, 2, lerp),
				new NoiseLayer(r3.nextLong(), 3000, 1, 2, lerp), new NoiseLayer(r3.nextLong(), 2000, 1, 2, cos),
				new NoiseLayer(r3.nextLong(), 1000, 1, 2, cos), new NoiseLayer(r3.nextLong(), 500, 1, 2, lerp),
				new NoiseLayer(r3.nextLong(), 300, 1, 2, lerp), new NoiseLayer(r3.nextLong(), 200, 1, 2, cos),
				new NoiseLayer(r3.nextLong(), 50, 1, 2, cos), new NoiseLayer(r3.nextLong(), 20, 1, 2, lerp),
				new NoiseLayer(r3.nextLong(), 5, 1, 2, lerp), new NoiseLayer(r3.nextLong(), 1, 1, 2, lerp)
			});
	}
}
