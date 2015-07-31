package com.wraithavens.conquest.SinglePlayer.Heightmap;

class HeightMap{
	final int posX;
	final int posZ;
	final HeightmapTexture heightmapTexture;
	HeightMap(int posX, int posZ, HeightmapTexture heightmapTexture){
		this.posX = posX;
		this.posZ = posZ;
		this.heightmapTexture = heightmapTexture;
	}
	void dispose(){
		heightmapTexture.dispose();
	}
}
