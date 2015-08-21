package com.wraithavens.conquest.SinglePlayer.Noise;

import com.wraithavens.conquest.Math.Vector3f;

class ColorNoise{
	private final SubNoise red;
	private final SubNoise green;
	private final SubNoise blue;
	ColorNoise(SubNoise red, SubNoise green, SubNoise blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	void noise(float x, float y, float z, Vector3f out){
		out.set((float)red.noise(x, y, z), (float)green.noise(x, y, z), (float)blue.noise(x, y, z));
	}
	void noise(float x, float y, Vector3f out){
		out.set((float)red.noise(x, y), (float)green.noise(x, y), (float)blue.noise(x, y));
	}
}
