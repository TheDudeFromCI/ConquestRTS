package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector3f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;

public class ChunkXQuadCounter implements QuadCounter{
	private static final float[] COLORS            = new float[ 3];
	private static final float[] TEXTURE_POSITIONS = new float[ 8];
	private static final float[] QUAD_POINTS       = new float[12];
	private static Matrix4f mat1  = new Matrix4f();
	private static Matrix4f mat2  = new Matrix4f();
	private static Matrix4f mat3  = new Matrix4f();
	private static Vector4f temp1 = new Vector4f();
	private static Vector4f temp2 = new Vector4f();
	private static Vector4f temp3 = new Vector4f();
	private static Vector4f temp4 = new Vector4f();
	static{
		ChunkXQuadCounter.mat1.rotate((float)Math.toRadians( 90), Vector3f.Z_AXIS);
		ChunkXQuadCounter.mat2.rotate((float)Math.toRadians(180), Vector3f.Z_AXIS);
		ChunkXQuadCounter.mat3.rotate((float)Math.toRadians(270), Vector3f.Z_AXIS);
	}
	private QuadBatch batch;
	private QuadListener listener;
	private int x, side, startX, startY, startZ, r, m, extraOffsetY;
	public void addQuad(int y, int z, int w, int h){
		float smallX = x/8f+startX;
		float smallY = y/8f+startY+extraOffsetY/8f;
		float smallZ = z/8f+startZ;
		float bigX   = smallX+1/8f;
		float bigY   = smallY+w/8f;
		float bigZ   = smallZ+h/8f;
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
			next(    8-z,   y);
			next(    8-z, y+w);
			next(8-(z+h), y+w);
			next(8-(z+h),   y);
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
		rot();
		if(listener!=null)listener.addQuad(new Quad(ChunkXQuadCounter.QUAD_POINTS, ChunkXQuadCounter.COLORS, ChunkXQuadCounter.TEXTURE_POSITIONS, side));
		else batch.addQuad(new Quad(ChunkXQuadCounter.QUAD_POINTS, ChunkXQuadCounter.COLORS, ChunkXQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkXQuadCounter.TEXTURE_POSITIONS[  m] = y/8f;
		ChunkXQuadCounter.TEXTURE_POSITIONS[m+1] = x/8f;
		m = (m+2)%8;
	}
	private void rot(){
		if(r==0) return;
		float half = Chunk.BLOCKS_PER_CHUNK/2f;
		ChunkXQuadCounter.temp1.set(ChunkXQuadCounter.TEXTURE_POSITIONS[0]-half, ChunkXQuadCounter.TEXTURE_POSITIONS[1]-half, 0, 1);
		ChunkXQuadCounter.temp2.set(ChunkXQuadCounter.TEXTURE_POSITIONS[2]-half, ChunkXQuadCounter.TEXTURE_POSITIONS[3]-half, 0, 1);
		ChunkXQuadCounter.temp3.set(ChunkXQuadCounter.TEXTURE_POSITIONS[4]-half, ChunkXQuadCounter.TEXTURE_POSITIONS[5]-half, 0, 1);
		ChunkXQuadCounter.temp4.set(ChunkXQuadCounter.TEXTURE_POSITIONS[6]-half, ChunkXQuadCounter.TEXTURE_POSITIONS[7]-half, 0, 1);
		if(r==1){
			Matrix4f.transform(ChunkXQuadCounter.mat1, ChunkXQuadCounter.temp1, ChunkXQuadCounter.temp1);
			Matrix4f.transform(ChunkXQuadCounter.mat1, ChunkXQuadCounter.temp2, ChunkXQuadCounter.temp2);
			Matrix4f.transform(ChunkXQuadCounter.mat1, ChunkXQuadCounter.temp3, ChunkXQuadCounter.temp3);
			Matrix4f.transform(ChunkXQuadCounter.mat1, ChunkXQuadCounter.temp4, ChunkXQuadCounter.temp4);
		}else if(r==2){
			Matrix4f.transform(ChunkXQuadCounter.mat2, ChunkXQuadCounter.temp1, ChunkXQuadCounter.temp1);
			Matrix4f.transform(ChunkXQuadCounter.mat2, ChunkXQuadCounter.temp2, ChunkXQuadCounter.temp2);
			Matrix4f.transform(ChunkXQuadCounter.mat2, ChunkXQuadCounter.temp3, ChunkXQuadCounter.temp3);
			Matrix4f.transform(ChunkXQuadCounter.mat2, ChunkXQuadCounter.temp4, ChunkXQuadCounter.temp4);
		}else{
			Matrix4f.transform(ChunkXQuadCounter.mat3, ChunkXQuadCounter.temp1, ChunkXQuadCounter.temp1);
			Matrix4f.transform(ChunkXQuadCounter.mat3, ChunkXQuadCounter.temp2, ChunkXQuadCounter.temp2);
			Matrix4f.transform(ChunkXQuadCounter.mat3, ChunkXQuadCounter.temp3, ChunkXQuadCounter.temp3);
			Matrix4f.transform(ChunkXQuadCounter.mat3, ChunkXQuadCounter.temp4, ChunkXQuadCounter.temp4);
		}
		ChunkXQuadCounter.TEXTURE_POSITIONS[0] = ChunkXQuadCounter.temp1.x+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[1] = ChunkXQuadCounter.temp1.y+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[2] = ChunkXQuadCounter.temp2.x+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[3] = ChunkXQuadCounter.temp2.y+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[4] = ChunkXQuadCounter.temp3.x+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[5] = ChunkXQuadCounter.temp3.y+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[6] = ChunkXQuadCounter.temp4.x+half;
		ChunkXQuadCounter.TEXTURE_POSITIONS[7] = ChunkXQuadCounter.temp4.y+half;
	}
	public void setup(int startX, int startY, int startZ, int x, int side, QuadBatch batch, int r, float red, float green, float blue){
		this.x      =      x;
		this.side   =   side;
		this.batch  =  batch;
		this.listener = null;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r      =      r;
		this.extraOffsetY = 0;
		ChunkXQuadCounter.COLORS[0] = red;
		ChunkXQuadCounter.COLORS[1] = green;
		ChunkXQuadCounter.COLORS[2] = blue;
	}
	public void setup(int startX, int startY, int startZ, int x, int side, QuadListener listener, int r, float red, float green, float blue, int extraOffsetY){
		this.x      =      x;
		this.side   =   side;
		this.batch  =  null;
		this.listener = listener;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r      =      r;
		this.extraOffsetY = extraOffsetY;
		ChunkXQuadCounter.COLORS[0] = red;
		ChunkXQuadCounter.COLORS[1] = green;
		ChunkXQuadCounter.COLORS[2] = blue;
	}
}