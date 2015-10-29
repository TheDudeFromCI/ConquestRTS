package com.wraithavens.conquest.Utility;

public class LinearInterpolation implements InterpolationFunction{
	public float interpolate(float a, float b, float frac){
		return (a-b)*frac+b;
	}
}
