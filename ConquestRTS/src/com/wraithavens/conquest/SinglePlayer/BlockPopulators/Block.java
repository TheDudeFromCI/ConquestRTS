package com.wraithavens.conquest.SinglePlayer.BlockPopulators;


public enum Block{
	Grass(new BlockTextures[]{
		BlockTextures.SideDirt, BlockTextures.SideDirt, BlockTextures.Grass, BlockTextures.Dirt,
		BlockTextures.SideDirt, BlockTextures.SideDirt
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
	public int getTexture(int j){
		switch(j){
			case 0:
				return texture0;
			case 1:
				return texture1;
			case 2:
				return texture2;
			case 3:
				return texture3;
			case 4:
				return texture4;
			case 5:
				return texture5;
			default:
				throw new AssertionError();
		}
	}
}
