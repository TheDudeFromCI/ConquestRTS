package com.wraithavens.conquest.SinglePlayer;

public class ChunkZQuadCounter implements QuadCounter{
	private static final float[] TEXTURE_POSITIONS = new float[ 8];
	private static final float[] QUAD_POINTS       = new float[12];
	private QuadListener listener;
	private int z, side, startX, startY, startZ, m;
	private final boolean full;
	public ChunkZQuadCounter(boolean full){
		this.full = full;
	}
	public void addQuad(int x, int y, int w, int h){
		float smallX = full?startX+x:x/8f+startX;
		float smallY = full?startY+y:y/8f+startY;
		float smallZ = full?startZ+z:z/8f+startZ;
		float bigX   = full?smallX+w:smallX+w/8f;
		float bigY   = full?smallY+h:smallY+h/8f;
		float bigZ   = full?smallZ+1:smallZ+1/8f;
		if(side==4){
			ChunkZQuadCounter.QUAD_POINTS[ 0] =   bigX;
			ChunkZQuadCounter.QUAD_POINTS[ 1] =   bigY;
			ChunkZQuadCounter.QUAD_POINTS[ 2] =   bigZ;
			ChunkZQuadCounter.QUAD_POINTS[ 3] = smallX;
			ChunkZQuadCounter.QUAD_POINTS[ 4] =   bigY;
			ChunkZQuadCounter.QUAD_POINTS[ 5] =   bigZ;
			ChunkZQuadCounter.QUAD_POINTS[ 6] = smallX;
			ChunkZQuadCounter.QUAD_POINTS[ 7] = smallY;
			ChunkZQuadCounter.QUAD_POINTS[ 8] =   bigZ;
			ChunkZQuadCounter.QUAD_POINTS[ 9] =   bigX;
			ChunkZQuadCounter.QUAD_POINTS[10] = smallY;
			ChunkZQuadCounter.QUAD_POINTS[11] =   bigZ;
			m = 4;
			if(full){
				next(y, x);
				next(y, x+w);
				next(y+h, x+w);
				next(y+h, x);
			}else{
				next(    8-y,   x);
				next(    8-y, x+w);
				next(8-(y+h), x+w);
				next(8-(y+h),   x);
			}
		}else{
			ChunkZQuadCounter.QUAD_POINTS[ 0] = smallX;
			ChunkZQuadCounter.QUAD_POINTS[ 1] = smallY;
			ChunkZQuadCounter.QUAD_POINTS[ 2] = smallZ;
			ChunkZQuadCounter.QUAD_POINTS[ 3] = smallX;
			ChunkZQuadCounter.QUAD_POINTS[ 4] = bigY;
			ChunkZQuadCounter.QUAD_POINTS[ 5] = smallZ;
			ChunkZQuadCounter.QUAD_POINTS[ 6] = bigX;
			ChunkZQuadCounter.QUAD_POINTS[ 7] = bigY;
			ChunkZQuadCounter.QUAD_POINTS[ 8] = smallZ;
			ChunkZQuadCounter.QUAD_POINTS[ 9] = bigX;
			ChunkZQuadCounter.QUAD_POINTS[10] = smallY;
			ChunkZQuadCounter.QUAD_POINTS[11] = smallZ;
			m = 0;
			next(  y,   x);
			next(y+h,   x);
			next(y+h, x+w);
			next(  y, x+w);
		}
		listener.addQuad(new Quad(ChunkZQuadCounter.QUAD_POINTS, ChunkZQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkZQuadCounter.TEXTURE_POSITIONS[  m] = full?y:y/8f;
		ChunkZQuadCounter.TEXTURE_POSITIONS[m+1] = full?x:x/8f;
		m = (m+2)%8;
	}
	public void setup(int startX, int startY, int startZ, int z, int side, QuadListener listener){
		this.z      =      z;
		this.side   =   side;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
	}
}