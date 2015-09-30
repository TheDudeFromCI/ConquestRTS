package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public class ChunkYQuadCounter implements QuadCounter{
	private static final float[] QuadPoints = new float[12];
	private static final float[] TextureCoords = new float[8];
	private QuadListener listener;
	private int y;
	private int side;
	private int startX;
	private int startY;
	private int startZ;
	private int texture;
	public void addQuad(int x, int z, int w, int h){
		float smallX = startX+x;
		float smallY = startY+y;
		float smallZ = startZ+z;
		float bigX = smallX+w;
		float bigY = smallY+1;
		float bigZ = smallZ+h;
		if(side==2){
			QuadPoints[0] = smallX;
			QuadPoints[1] = bigY;
			QuadPoints[2] = smallZ;
			QuadPoints[3] = smallX;
			QuadPoints[4] = bigY;
			QuadPoints[5] = bigZ;
			QuadPoints[6] = bigX;
			QuadPoints[7] = bigY;
			QuadPoints[8] = bigZ;
			QuadPoints[9] = bigX;
			QuadPoints[10] = bigY;
			QuadPoints[11] = smallZ;
			TextureCoords[0] = 0;
			TextureCoords[1] = 0;
			TextureCoords[2] = 0;
			TextureCoords[3] = h;
			TextureCoords[4] = w;
			TextureCoords[5] = h;
			TextureCoords[6] = w;
			TextureCoords[7] = 0;
		}else{
			QuadPoints[0] = bigX;
			QuadPoints[1] = smallY;
			QuadPoints[2] = bigZ;
			QuadPoints[3] = smallX;
			QuadPoints[4] = smallY;
			QuadPoints[5] = bigZ;
			QuadPoints[6] = smallX;
			QuadPoints[7] = smallY;
			QuadPoints[8] = smallZ;
			QuadPoints[9] = bigX;
			QuadPoints[10] = smallY;
			QuadPoints[11] = smallZ;
			TextureCoords[0] = 0;
			TextureCoords[1] = 0;
			TextureCoords[2] = 0;
			TextureCoords[3] = w;
			TextureCoords[4] = h;
			TextureCoords[5] = w;
			TextureCoords[6] = h;
			TextureCoords[7] = 0;
		}
		listener.addQuad(new Quad(QuadPoints, TextureCoords, texture, side, texture==BlockTextures.Grass
			.ordinal()));
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadListener listener, Block block){
		this.y = y;
		this.side = side;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		texture = side==2?block.texture2:block.texture3;
	}
}
