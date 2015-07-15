package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public enum Block{
	GRASS(0.0f, 0.6f, 0.0f);
	public static final byte AIR = -128;
	public static final int ID_SHIFT = 127;
	public final float red;
	public final float green;
	public final float blue;
	private Block(float red, float green, float blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
}