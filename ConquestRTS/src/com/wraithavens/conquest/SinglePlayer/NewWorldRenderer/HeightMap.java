package com.wraithavens.conquest.SinglePlayer.NewWorldRenderer;

class HeightMap{
	final int posX;
	final int posZ;
	final HeightmapTexture texture;
	HeightMap(int posX, int posZ, HeightmapTexture texture){
		this.posX = posX;
		this.posZ = posZ;
		this.texture = texture;
	}
	void dispose(){
		texture.dispose();
	}
}
