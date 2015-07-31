package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;

public class ColorNoise{
	private final SubNoise red;
	private final SubNoise green;
	private final SubNoise blue;
	public ColorNoise(SubNoise red, SubNoise green, SubNoise blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	public void noise(float x, float y, Vector3f out){
		out.set((float)red.noise(x, y), (float)green.noise(x, y), (float)blue.noise(x, y));
	}
}
