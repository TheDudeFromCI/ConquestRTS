package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public class ChunkXQuadCounter implements QuadCounter{
	private static final float[] QuadPoints = new float[12];
	private static final float[] TextureCoords = new float[8];
	private QuadListener listener;
	private int x;
	private int side;
	private int startX;
	private int startY;
	private int startZ;
	private int texture;
	public void addQuad(int y, int z, int w, int h){
		float smallX = startX+x;
		float smallY = startY+y;
		float smallZ = startZ+z;
		float bigX = smallX+1;
		float bigY = smallY+w;
		float bigZ = smallZ+h;
		if(side==0){
			QuadPoints[0] = bigX;
			QuadPoints[1] = bigY;
			QuadPoints[2] = bigZ;
			QuadPoints[3] = bigX;
			QuadPoints[4] = smallY;
			QuadPoints[5] = bigZ;
			QuadPoints[6] = bigX;
			QuadPoints[7] = smallY;
			QuadPoints[8] = smallZ;
			QuadPoints[9] = bigX;
			QuadPoints[10] = bigY;
			QuadPoints[11] = smallZ;
			TextureCoords[0] = 0;
			TextureCoords[1] = 0;
			TextureCoords[2] = 0;
			TextureCoords[3] = w;
			TextureCoords[4] = h;
			TextureCoords[5] = w;
			TextureCoords[6] = h;
			TextureCoords[7] = 0;
		}else{
			QuadPoints[0] = smallX;
			QuadPoints[1] = smallY;
			QuadPoints[2] = smallZ;
			QuadPoints[3] = smallX;
			QuadPoints[4] = smallY;
			QuadPoints[5] = bigZ;
			QuadPoints[6] = smallX;
			QuadPoints[7] = bigY;
			QuadPoints[8] = bigZ;
			QuadPoints[9] = smallX;
			QuadPoints[10] = bigY;
			QuadPoints[11] = smallZ;
			TextureCoords[0] = w;
			TextureCoords[1] = h;
			TextureCoords[2] = 0;
			TextureCoords[3] = h;
			TextureCoords[4] = 0;
			TextureCoords[5] = 0;
			TextureCoords[6] = w;
			TextureCoords[7] = 0;
		}
		listener.addQuad(new Quad(QuadPoints, TextureCoords, texture, side));
	}
	public void setup(int startX, int startY, int startZ, int x, int side, QuadListener listener, Block block){
		this.x = x;
		this.side = side;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		texture = side==0?block.texture0:block.texture1;
	}
}
