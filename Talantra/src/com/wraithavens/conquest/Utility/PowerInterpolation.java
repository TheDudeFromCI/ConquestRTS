package com.wraithavens.conquest.Utility;

public class PowerInterpolation implements InterpolationFunction{
	private final float power;
	public PowerInterpolation(float power){
		this.power = power;
	}
	public float interpolate(float a, float b, float frac){
		frac = (float)Math.pow(frac, power);
		return a*frac+(1.0f-frac)*b;
	}
}
