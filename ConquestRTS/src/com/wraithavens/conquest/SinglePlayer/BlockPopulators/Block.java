package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public enum Block{
	Grass(new BlockTextures[]{
		BlockTextures.Dirt, BlockTextures.Dirt, BlockTextures.Grass, BlockTextures.Dirt, BlockTextures.Dirt,
		BlockTextures.Dirt
	});
	final int texture0;
	final int texture1;
	final int texture2;
	final int texture3;
	final int texture4;
	final int texture5;
	private Block(BlockTextures[] textures){
		texture0 = textures[0].ordinal();
		texture1 = textures[1].ordinal();
		texture2 = textures[2].ordinal();
		texture3 = textures[3].ordinal();
		texture4 = textures[4].ordinal();
		texture5 = textures[5].ordinal();
	}
}
