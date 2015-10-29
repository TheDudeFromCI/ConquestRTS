package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.util.ArrayList;
import com.wraithavens.conquest.Utility.Algorithms;

public class ChunkBlocks{
	private final ArrayList<ChunkBlocksSegment> blocks = new ArrayList();
	private final int x;
	private final int z;
	public ChunkBlocks(int x, int z){
		this.x = x;
		this.z = z;
	}
	public byte getBlock(int x, int y, int z){
		int chunkY = Algorithms.groupLocation(y, 32);
		x -= this.x;
		y -= chunkY;
		z -= this.z;
		for(ChunkBlocksSegment seg : blocks)
			if(seg.getChunkY()==chunkY)
				return seg.getBlock(x, y, z);
		ChunkBlocksSegment seg = new ChunkBlocksSegment(this.x, chunkY, this.z);
		blocks.add(seg);
		return seg.getBlock(x, y, z);
	}
	/**
	 * Saved all blocks to file, and returns the verticle coordinates of all
	 * chunks that have changed. This method also dumps all memory it's
	 * currently using, so call this method after editing, not during.
	 */
	public ArrayList<Integer> save(){
		ArrayList<Integer> changed = new ArrayList();
		for(ChunkBlocksSegment seg : blocks)
			if(seg.save())
				changed.add(seg.getChunkY());
		blocks.clear();
		return changed;
	}
	public void setBlock(int x, int y, int z, byte type){
		int chunkY = Algorithms.groupLocation(y, 32);
		x -= this.x;
		y -= chunkY;
		z -= this.z;
		for(ChunkBlocksSegment seg : blocks)
			if(seg.getChunkY()==chunkY){
				seg.setBlock(x, y, z, type);
				return;
			}
		ChunkBlocksSegment seg = new ChunkBlocksSegment(this.x, chunkY, this.z);
		blocks.add(seg);
		seg.setBlock(x, y, z, type);
	}
}
