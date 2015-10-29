package com.wraithavens.conquest.SinglePlayer.Blocks.World;

public class ChunkLoadQue{
	private boolean waiting = false;
	private int x;
	private int z;
	public void addQue(int x, int z){
		this.x = x;
		this.z = z;
		waiting = true;
	}
	public void finish(){
		waiting = false;
	}
	public int getX(){
		return x;
	}
	public int getZ(){
		return z;
	}
	public boolean isWaiting(){
		return waiting;
	}
}
