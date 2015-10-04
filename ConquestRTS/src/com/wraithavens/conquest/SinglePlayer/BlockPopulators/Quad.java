package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.nio.FloatBuffer;

public class Quad{
	private final FloatBuffer data; // Vertices (0-11), Texture Coords (12-19),
	Quad(float[] points, float[] textureCoords, int texture){
		data = FloatBuffer.allocate(21);
		data.put(points);
		data.put(textureCoords);
		data.put(texture);
	}
}
