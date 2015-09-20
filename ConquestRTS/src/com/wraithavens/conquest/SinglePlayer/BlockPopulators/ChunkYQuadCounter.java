package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

public class ChunkYQuadCounter implements QuadCounter{
	private static final float[] QUAD_POINTS = new float[12];
	private static final float[] COLORS = new float[3];
	private QuadListener listener;
	private int y, side, startX, startY, startZ;
	public void addQuad(int x, int z, int w, int h){
		float smallX = startX+x;
		float smallY = startY+y;
		float smallZ = startZ+z;
		float bigX = smallX+w;
		float bigY = smallY+1;
		float bigZ = smallZ+h;
		if(side==2){
			QUAD_POINTS[0] = smallX;
			QUAD_POINTS[1] = bigY;
			QUAD_POINTS[2] = smallZ;
			QUAD_POINTS[3] = smallX;
			QUAD_POINTS[4] = bigY;
			QUAD_POINTS[5] = bigZ;
			QUAD_POINTS[6] = bigX;
			QUAD_POINTS[7] = bigY;
			QUAD_POINTS[8] = bigZ;
			QUAD_POINTS[9] = bigX;
			QUAD_POINTS[10] = bigY;
			QUAD_POINTS[11] = smallZ;
		}else{
			QUAD_POINTS[0] = bigX;
			QUAD_POINTS[1] = smallY;
			QUAD_POINTS[2] = bigZ;
			QUAD_POINTS[3] = smallX;
			QUAD_POINTS[4] = smallY;
			QUAD_POINTS[5] = bigZ;
			QUAD_POINTS[6] = smallX;
			QUAD_POINTS[7] = smallY;
			QUAD_POINTS[8] = smallZ;
			QUAD_POINTS[9] = bigX;
			QUAD_POINTS[10] = smallY;
			QUAD_POINTS[11] = smallZ;
		}
		listener.addQuad(new Quad(QUAD_POINTS, COLORS, side));
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadListener listener, Block block){
		this.y = y;
		this.side = side;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		COLORS[0] = block.red;
		COLORS[1] = block.green;
		COLORS[2] = block.blue;
	}
}
