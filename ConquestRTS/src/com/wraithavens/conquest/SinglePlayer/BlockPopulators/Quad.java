package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.nio.FloatBuffer;

public class Quad{
	public final FloatBuffer data; // Vertices (0-11), Texture Coords (12-19),
	// Texture Id (20)
	public final int side;
	public final boolean grass;
	Quad(float[] points, float[] textureCoords, int texture, int side, boolean grass){
		data = FloatBuffer.allocate(21);
		data.put(points);
		data.put(textureCoords);
		data.put(texture);
		this.side = side;
		this.grass = grass;
	}
}
