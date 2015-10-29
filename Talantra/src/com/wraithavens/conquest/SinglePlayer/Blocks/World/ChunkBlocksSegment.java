package com.wraithavens.conquest.SinglePlayer.Blocks.World;

import java.io.File;
import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.BlockClipData;
import com.wraithavens.conquest.Utility.Algorithms;
import com.wraithavens.conquest.Utility.BinaryFile;

public class ChunkBlocksSegment{
	static final byte Air = -1;
	private final byte[] blocks = new byte[32*32*32];
	private final BlockClipData clipData;
	private final int chunkX;
	private final int chunkY;
	private final int chunkZ;
	private boolean changed;
	public ChunkBlocksSegment(int chunkX, int chunkY, int chunkZ){
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		clipData = new BlockClipData();
		File file = Algorithms.getChunkBlocksPath(chunkX, chunkY, chunkZ);
		if(file.exists()&&file.length()>0){
			BinaryFile bin = new BinaryFile(file);
			bin.decompress(false);
			bin.getBytes(blocks);
			bin.getBytes(clipData.getBytes());
		}
	}
	public void fill(byte type){
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = type;
	}
	public byte getBlock(int x, int y, int z){
		return blocks[x*32*32+y*32+z];
	}
	public int getChunkY(){
		return chunkY;
	}
	public boolean save(){
		if(!changed)
			return false;
		changed = false;
		BinaryFile bin = new BinaryFile(blocks.length+clipData.getBytes().length);
		bin.addBytes(blocks, 0, blocks.length);
		bin.addBytes(clipData.getBytes(), 0, clipData.getBytes().length);
		bin.compress(false);
		bin.compile(Algorithms.getChunkBlocksPath(chunkX, chunkY, chunkZ));
		return true;
	}
	public void setBlock(int x, int y, int z, byte b){
		changed = true;
		if(x>=0&&x<32&&y>=0&&y<32&&z>=0&&z<32)
			blocks[x*32*32+y*32+z] = b;
		else
			clipData.setHasBlockWeak(x, y, z, b);
	}
}
