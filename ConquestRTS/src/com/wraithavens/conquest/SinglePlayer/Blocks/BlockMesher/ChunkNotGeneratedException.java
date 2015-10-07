package com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher;

public class ChunkNotGeneratedException extends Exception{
	public ChunkNotGeneratedException(int x, int y, int z){
		super("Chunk: ["+x+", "+y+", "+z+"]");
	}
}
