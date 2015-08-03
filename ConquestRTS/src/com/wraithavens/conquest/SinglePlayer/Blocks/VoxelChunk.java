package com.wraithavens.conquest.SinglePlayer.Blocks;

public class VoxelChunk{
	protected final int x;
	protected final int y;
	protected final int z;
	protected final int size;
	public VoxelChunk(int x, int y, int z, int size){
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
	}
	public final boolean containsBlock(int x, int y, int z){
		return x>=this.x&&y>=this.y&&z>=this.z&&x<this.x+size&&y<this.y+size&&z<this.z+size;
	}
	public final int getSize(){
		return size;
	}
	public final int getX(){
		return x;
	}
	public final int getY(){
		return y;
	}
	public final int getZ(){
		return z;
	}
}
