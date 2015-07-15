package com.wraithavens.conquest.SinglePlayer;

import java.nio.FloatBuffer;

public class Quad{
	public final FloatBuffer data; //Vertices (0-11), Color(12-14)
	final int side;
	Quad(float[] points, float[] color, int side){
		data = FloatBuffer.allocate(20);
		data.put(points);
		data.put(color);
		this.side = side;
	}
}