package com.wraithavens.conquest.Utility;


public class Algorithms{
	public static String formatByteCount(long byteCount){
		if(byteCount<1024)
			return byteCount+" B";
		if(byteCount<1024*1024)
			return String.format("%.2f", (float)(byteCount/1024.0))+" KB";
		if(byteCount<1024*1024*1024)
			return String.format("%.2f", (float)(byteCount/(1024.0*1024.0)))+" MB";
		return String.format("%.2f", (float)(byteCount/(1024.0*1024.0*1024.0)))+" GB";
	}
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
