package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public enum Block{
	GRASS(1.0f, 1.0f, 1.0f);
	public static final int ID_SHIFT = 127;
	final float red;
	final float green;
	final float blue;
	private Block(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
}
