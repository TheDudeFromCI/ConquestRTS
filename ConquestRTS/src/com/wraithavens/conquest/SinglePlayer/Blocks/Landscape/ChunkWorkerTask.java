package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

public class ChunkWorkerTask{
	private final int x;
	private final int y;
	private final int z;
	private volatile boolean finished = false;
	ChunkWorkerTask(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
	boolean isFinished(){
		return finished;
	}
	void setFinished(){
		finished = true;
	}
}
