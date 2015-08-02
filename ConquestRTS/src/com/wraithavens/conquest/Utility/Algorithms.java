package com.wraithavens.conquest.Utility;

public class Algorithms{
	/**
	 * This function takes any value, <i>x</i>, and groups it into evenly sized
	 * chunks.
	 *
	 * @param x
	 *            - The location.
	 * @param w
	 *            - The chunk size.
	 * @return The grouped value of x.
	 */
	public static int groupLocation(int x, int w){
		return x>=0?x/w*w:(x-(w-1))/w*w;
	}
}
