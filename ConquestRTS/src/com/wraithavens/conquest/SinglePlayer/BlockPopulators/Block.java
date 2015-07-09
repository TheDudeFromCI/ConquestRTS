package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;

enum Block{
	GRASS("Grass.png");
	static void loadTextures(){
		Block[] blocks = Block.values();
		for(int i = 0; i<blocks.length; i++)
			blocks[i].loadTexture();
	}
	public static final byte AIR = -128;
	public static final int ID_SHIFT = 127;
	private Texture texture;
	private final String textureName;
	private Block(String textureName){
		this.textureName = textureName;
	}
	Texture getTexture(){
		return texture;
	}
	private void loadTexture(){
		if(texture!=null)texture.dispose();
		texture = Texture.getTexture(WraithavensConquest.textureFolder, textureName, 6);
	}
}