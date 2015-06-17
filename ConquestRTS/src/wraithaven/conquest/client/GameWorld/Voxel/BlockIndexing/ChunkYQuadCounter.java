package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector3f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;

public class ChunkYQuadCounter implements QuadCounter{
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
		ChunkYQuadCounter.mat1.rotate((float)Math.toRadians( 90), Vector3f.Z_AXIS);
		ChunkYQuadCounter.mat2.rotate((float)Math.toRadians(180), Vector3f.Z_AXIS);
		ChunkYQuadCounter.mat3.rotate((float)Math.toRadians(270), Vector3f.Z_AXIS);
	}
	private QuadBatch batch;
	private QuadListener listener;
	private int y, side, startX, startY, startZ, r, m;
	public void addQuad(int x, int z, int w, int h){
		float smallX = x/8f+startX;
		float smallY = y/8f+startY;
		float smallZ = z/8f+startZ;
		float bigX   = smallX+w/8f;
		float bigY   = smallY+1/8f;
		float bigZ   = smallZ+h/8f;
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
			next(8-(x+w), z+h);
			next(8-(x+w),   z);
			next(    8-x,   z);
			next(    8-x, z+h);
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
			next(z+h,   x);
			next(  z,   x);
			next(  z, x+w);
		}
		rot();
		if(listener!=null)listener.addQuad(new Quad(ChunkYQuadCounter.QUAD_POINTS, ChunkYQuadCounter.COLORS, ChunkYQuadCounter.TEXTURE_POSITIONS, side));
		else batch.addQuad(new Quad(ChunkYQuadCounter.QUAD_POINTS, ChunkYQuadCounter.COLORS, ChunkYQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkYQuadCounter.TEXTURE_POSITIONS[  m] = y/8f;
		ChunkYQuadCounter.TEXTURE_POSITIONS[m+1] = x/8f;
		m = (m+2)%8;
	}
	private void rot(){
		if(r==0) return;
		float half = Chunk.BLOCKS_PER_CHUNK/2f;
		ChunkYQuadCounter.temp1.set(ChunkYQuadCounter.TEXTURE_POSITIONS[0]-half, ChunkYQuadCounter.TEXTURE_POSITIONS[1]-half, 0, 1);
		ChunkYQuadCounter.temp2.set(ChunkYQuadCounter.TEXTURE_POSITIONS[2]-half, ChunkYQuadCounter.TEXTURE_POSITIONS[3]-half, 0, 1);
		ChunkYQuadCounter.temp3.set(ChunkYQuadCounter.TEXTURE_POSITIONS[4]-half, ChunkYQuadCounter.TEXTURE_POSITIONS[5]-half, 0, 1);
		ChunkYQuadCounter.temp4.set(ChunkYQuadCounter.TEXTURE_POSITIONS[6]-half, ChunkYQuadCounter.TEXTURE_POSITIONS[7]-half, 0, 1);
		if(r==1){
			Matrix4f.transform(ChunkYQuadCounter.mat1, ChunkYQuadCounter.temp1, ChunkYQuadCounter.temp1);
			Matrix4f.transform(ChunkYQuadCounter.mat1, ChunkYQuadCounter.temp2, ChunkYQuadCounter.temp2);
			Matrix4f.transform(ChunkYQuadCounter.mat1, ChunkYQuadCounter.temp3, ChunkYQuadCounter.temp3);
			Matrix4f.transform(ChunkYQuadCounter.mat1, ChunkYQuadCounter.temp4, ChunkYQuadCounter.temp4);
		}else if(r==2){
			Matrix4f.transform(ChunkYQuadCounter.mat2, ChunkYQuadCounter.temp1, ChunkYQuadCounter.temp1);
			Matrix4f.transform(ChunkYQuadCounter.mat2, ChunkYQuadCounter.temp2, ChunkYQuadCounter.temp2);
			Matrix4f.transform(ChunkYQuadCounter.mat2, ChunkYQuadCounter.temp3, ChunkYQuadCounter.temp3);
			Matrix4f.transform(ChunkYQuadCounter.mat2, ChunkYQuadCounter.temp4, ChunkYQuadCounter.temp4);
		}else{
			Matrix4f.transform(ChunkYQuadCounter.mat3, ChunkYQuadCounter.temp1, ChunkYQuadCounter.temp1);
			Matrix4f.transform(ChunkYQuadCounter.mat3, ChunkYQuadCounter.temp2, ChunkYQuadCounter.temp2);
			Matrix4f.transform(ChunkYQuadCounter.mat3, ChunkYQuadCounter.temp3, ChunkYQuadCounter.temp3);
			Matrix4f.transform(ChunkYQuadCounter.mat3, ChunkYQuadCounter.temp4, ChunkYQuadCounter.temp4);
		}
		ChunkYQuadCounter.TEXTURE_POSITIONS[0] = ChunkYQuadCounter.temp1.x+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[1] = ChunkYQuadCounter.temp1.y+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[2] = ChunkYQuadCounter.temp2.x+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[3] = ChunkYQuadCounter.temp2.y+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[4] = ChunkYQuadCounter.temp3.x+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[5] = ChunkYQuadCounter.temp3.y+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[6] = ChunkYQuadCounter.temp4.x+half;
		ChunkYQuadCounter.TEXTURE_POSITIONS[7] = ChunkYQuadCounter.temp4.y+half;
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadBatch batch, int r, float red, float green, float blue){
		this.y      =      y;
		this.side   =   side;
		this.batch  =  batch;
		this.listener = null;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r      =      r;
		ChunkYQuadCounter.COLORS[0] = red;
		ChunkYQuadCounter.COLORS[1] = green;
		ChunkYQuadCounter.COLORS[2] = blue;
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadListener listener, int r, float red, float green, float blue){
		this.y      =      y;
		this.side   =   side;
		this.listener  =  listener;
		this.batch = null;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r      =      r;
		ChunkYQuadCounter.COLORS[0] = red;
		ChunkYQuadCounter.COLORS[1] = green;
		ChunkYQuadCounter.COLORS[2] = blue;
	}
}