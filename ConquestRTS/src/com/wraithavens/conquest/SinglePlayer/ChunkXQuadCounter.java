package com.wraithavens.conquest.SinglePlayer;

public class ChunkXQuadCounter implements QuadCounter{
	private static final float[] TEXTURE_POSITIONS = new float[ 8];
	private static final float[] QUAD_POINTS       = new float[12];
	private QuadListener listener;
	private int x, side, startX, startY, startZ, m;
	private final boolean full;
	public ChunkXQuadCounter(boolean full){
		this.full = full;
	}
	public void addQuad(int y, int z, int w, int h){
		float smallX = full?startX+x:x/8f+startX;
		float smallY = full?startY+y:y/8f+startY;
		float smallZ = full?startZ+z:z/8f+startZ;
		float bigX   = full?smallX+1:smallX+1/8f;
		float bigY   = full?smallY+w:smallY+w/8f;
		float bigZ   = full?smallZ+h:smallZ+h/8f;
		if(side==0){
			ChunkXQuadCounter.QUAD_POINTS[ 0] =   bigX;
			ChunkXQuadCounter.QUAD_POINTS[ 1] =   bigY;
			ChunkXQuadCounter.QUAD_POINTS[ 2] =   bigZ;
			ChunkXQuadCounter.QUAD_POINTS[ 3] =   bigX;
			ChunkXQuadCounter.QUAD_POINTS[ 4] = smallY;
			ChunkXQuadCounter.QUAD_POINTS[ 5] =   bigZ;
			ChunkXQuadCounter.QUAD_POINTS[ 6] =   bigX;
			ChunkXQuadCounter.QUAD_POINTS[ 7] = smallY;
			ChunkXQuadCounter.QUAD_POINTS[ 8] = smallZ;
			ChunkXQuadCounter.QUAD_POINTS[ 9] =   bigX;
			ChunkXQuadCounter.QUAD_POINTS[10] =   bigY;
			ChunkXQuadCounter.QUAD_POINTS[11] = smallZ;
			m = 4;
			if(full){
				next(z, y);
				next(z, y+w);
				next(z+h, y+w);
				next(z+h, y);
			}else{
				next(    8-z,   y);
				next(    8-z, y+w);
				next(8-(z+h), y+w);
				next(8-(z+h),   y);
			}
		}else{
			ChunkXQuadCounter.QUAD_POINTS[ 0] = smallX;
			ChunkXQuadCounter.QUAD_POINTS[ 1] = smallY;
			ChunkXQuadCounter.QUAD_POINTS[ 2] = smallZ;
			ChunkXQuadCounter.QUAD_POINTS[ 3] = smallX;
			ChunkXQuadCounter.QUAD_POINTS[ 4] = smallY;
			ChunkXQuadCounter.QUAD_POINTS[ 5] =   bigZ;
			ChunkXQuadCounter.QUAD_POINTS[ 6] = smallX;
			ChunkXQuadCounter.QUAD_POINTS[ 7] =   bigY;
			ChunkXQuadCounter.QUAD_POINTS[ 8] =   bigZ;
			ChunkXQuadCounter.QUAD_POINTS[ 9] = smallX;
			ChunkXQuadCounter.QUAD_POINTS[10] =   bigY;
			ChunkXQuadCounter.QUAD_POINTS[11] = smallZ;
			m = 0;
			next(  z,   y);
			next(z+h,   y);
			next(z+h, y+w);
			next(  z, y+w);
		}
		listener.addQuad(new Quad(ChunkXQuadCounter.QUAD_POINTS, ChunkXQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkXQuadCounter.TEXTURE_POSITIONS[  m] = full?y:y/8f;
		ChunkXQuadCounter.TEXTURE_POSITIONS[m+1] = full?x:x/8f;
		m = (m+2)%8;
	}
	public void setup(int startX, int startY, int startZ, int x, int side, QuadListener listener){
		this.x      =      x;
		this.side   =   side;
		this.listener  =  listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
	}
}