package com.wraithavens.conquest.SinglePlayer;

import java.nio.FloatBuffer;

public class Quad{
	public final FloatBuffer data; //Texture Points (0-7), Vertices (8-19);
	final int side;
	Quad(float[] points, float[] texturePositions, int side){
		data = FloatBuffer.allocate(20);
		data.put(texturePositions);
		data.put(points);
		this.side = side;
	}
}