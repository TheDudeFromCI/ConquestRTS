package com.wraithavens.conquest.SinglePlayer.Blocks;

import com.wraithavens.conquest.SinglePlayer.BlockPopulators.Block;
import com.wraithavens.conquest.SinglePlayer.Noise.WorldNoiseMachine;

public class ChunkGenerator{
	private final WorldNoiseMachine machine;
	public ChunkGenerator(WorldNoiseMachine machine){
		this.machine = machine;
	}
	public RawChunk generateRawChunk(int startX, int startY, int startZ){
		RawChunk raw = new RawChunk(startX, startY, startZ);
		int x, y, z, h;
		for(x = startX; x<startX+16; x++)
			for(z = startZ; z<startZ+16; z++){
				h = (int)machine.getWorldHeight(x, z);
				for(y = startY; y<=h&&y<startY+16; y++)
					raw.setBlock(x, y, z, Block.GRASS.id());
			}
		return raw;
	}
	public int getHeightAt(int x, int z){
		return (int)machine.getWorldHeight(x, z);
	}
}
