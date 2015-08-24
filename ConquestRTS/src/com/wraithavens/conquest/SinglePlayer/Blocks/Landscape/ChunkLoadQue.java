package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

public class ChunkLoadQue{
	private volatile int x;
	private volatile int y;
	private volatile int z;
	private volatile boolean waiting;
	public boolean check(){
		return waiting;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public void take(){
		waiting = false;
	}
	void placeAndWait(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		waiting = true;
		while(waiting)
			Thread.yield();
	}
}
