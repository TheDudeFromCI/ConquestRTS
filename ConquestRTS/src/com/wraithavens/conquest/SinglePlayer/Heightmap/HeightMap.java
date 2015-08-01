package com.wraithavens.conquest.SinglePlayer.Heightmap;

class HeightMap{
	private int posX;
	private int posZ;
	private HeightmapTexture heightmapTexture;
	public int getX(){
		return posX;
	}
	public int getZ(){
		return posZ;
	}
	void bind(){
		heightmapTexture.bind();
	}
	void dispose(){
		heightmapTexture.dispose();
	}
	boolean isNull(){
		return heightmapTexture==null;
	}
	void update(int x, int z, HeightmapTexture texture){
		posX = x;
		posZ = z;
		heightmapTexture = texture;
	}
}
