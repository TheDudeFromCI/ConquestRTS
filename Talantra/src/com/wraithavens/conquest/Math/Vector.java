package com.wraithavens.conquest.Math;

import java.nio.FloatBuffer;

abstract class Vector{
	public abstract float lengthSquared();
	public abstract Vector load(FloatBuffer buf);
	public abstract Vector negate();
	public abstract Vector scale(float scale);
	public abstract Vector store(FloatBuffer buf);
}