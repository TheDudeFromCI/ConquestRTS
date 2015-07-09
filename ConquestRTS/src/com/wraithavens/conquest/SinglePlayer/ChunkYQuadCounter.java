package com.wraithavens.conquest.SinglePlayer;


public class ChunkYQuadCounter implements QuadCounter{
	private static final float[] TEXTURE_POSITIONS = new float[ 8];
	private static final float[] QUAD_POINTS       = new float[12];
	private QuadListener listener;
	private int y, side, startX, startY, startZ, m;
	private final boolean full;
	public ChunkYQuadCounter(boolean full){
		this.full = full;
	}
	public void addQuad(int x, int z, int w, int h){
		float smallX = full?startX+x:x/8f+startX;
		float smallY = full?startY+y:y/8f+startY;
		float smallZ = full?startZ+z:z/8f+startZ;
		float bigX   = full?smallX+w:smallX+w/8f;
		float bigY   = full?smallY+1:smallY+1/8f;
		float bigZ   = full?smallZ+h:smallZ+h/8f;
		if(side==2){
			ChunkYQuadCounter.QUAD_POINTS[ 0] = smallX;
			ChunkYQuadCounter.QUAD_POINTS[ 1] =   bigY;
			ChunkYQuadCounter.QUAD_POINTS[ 2] = smallZ;
			ChunkYQuadCounter.QUAD_POINTS[ 3] = smallX;
			ChunkYQuadCounter.QUAD_POINTS[ 4] =   bigY;
			ChunkYQuadCounter.QUAD_POINTS[ 5] =   bigZ;
			ChunkYQuadCounter.QUAD_POINTS[ 6] =   bigX;
			ChunkYQuadCounter.QUAD_POINTS[ 7] =   bigY;
			ChunkYQuadCounter.QUAD_POINTS[ 8] =   bigZ;
			ChunkYQuadCounter.QUAD_POINTS[ 9] =   bigX;
			ChunkYQuadCounter.QUAD_POINTS[10] =   bigY;
			ChunkYQuadCounter.QUAD_POINTS[11] = smallZ;
			m = 4;
			if(full){
				next(x+w, z+h);
				next(x+w, z);
				next(x, z);
				next(x, z+h);
			}else{
				next(8-(x+w), z+h);
				next(8-(x+w),   z);
				next(    8-x,   z);
				next(    8-x, z+h);
			}
		}else{
			ChunkYQuadCounter.QUAD_POINTS[ 0] =   bigX;
			ChunkYQuadCounter.QUAD_POINTS[ 1] = smallY;
			ChunkYQuadCounter.QUAD_POINTS[ 2] =   bigZ;
			ChunkYQuadCounter.QUAD_POINTS[ 3] = smallX;
			ChunkYQuadCounter.QUAD_POINTS[ 4] = smallY;
			ChunkYQuadCounter.QUAD_POINTS[ 5] =   bigZ;
			ChunkYQuadCounter.QUAD_POINTS[ 6] = smallX;
			ChunkYQuadCounter.QUAD_POINTS[ 7] = smallY;
			ChunkYQuadCounter.QUAD_POINTS[ 8] = smallZ;
			ChunkYQuadCounter.QUAD_POINTS[ 9] =   bigX;
			ChunkYQuadCounter.QUAD_POINTS[10] = smallY;
			ChunkYQuadCounter.QUAD_POINTS[11] = smallZ;
			m = 0;
			next(z+h, x+w);
			next(z+h, x);
			next( z, x);
			next( z, x+w);
		}
		listener.addQuad(new Quad(ChunkYQuadCounter.QUAD_POINTS, ChunkYQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkYQuadCounter.TEXTURE_POSITIONS[  m] = full?y:y/8f;
		ChunkYQuadCounter.TEXTURE_POSITIONS[m+1] = full?x:x/8f;
		m = (m+2)%8;
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadListener listener){
		this.y      =      y;
		this.side   =   side;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
	}
}