package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector3f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;

public class ChunkZQuadCounter implements QuadCounter{
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
		ChunkZQuadCounter.mat1.rotate((float)Math.toRadians( 90), Vector3f.Z_AXIS);
		ChunkZQuadCounter.mat2.rotate((float)Math.toRadians(180), Vector3f.Z_AXIS);
		ChunkZQuadCounter.mat3.rotate((float)Math.toRadians(270), Vector3f.Z_AXIS);
	}
	private QuadBatch batch;
	private int z, side, startX, startY, startZ, r, m;
	public void addQuad(int x, int y, int w, int h){
		float smallX = x/8f+startX;
		float smallY = y/8f+startY;
		float smallZ = z/8f+startZ;
		float bigX   = smallX+w/8f;
		float bigY   = smallY+h/8f;
		float bigZ   = smallZ+1/8f;
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
			next(    8-y,   x);
			next(    8-y, x+w);
			next(8-(y+h), x+w);
			next(8-(y+h),   x);
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
		rot();
		batch.addQuad(new Quad(ChunkZQuadCounter.QUAD_POINTS, ChunkZQuadCounter.COLORS, ChunkZQuadCounter.TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		ChunkZQuadCounter.TEXTURE_POSITIONS[  m] = y/8f;
		ChunkZQuadCounter.TEXTURE_POSITIONS[m+1] = x/8f;
		m = (m+2)%8;
	}
	private void rot(){
		if(r==0) return;
		float half = Chunk.BLOCKS_PER_CHUNK/2f;
		ChunkZQuadCounter.temp1.set(ChunkZQuadCounter.TEXTURE_POSITIONS[0]-half, ChunkZQuadCounter.TEXTURE_POSITIONS[1]-half, 0, 1);
		ChunkZQuadCounter.temp2.set(ChunkZQuadCounter.TEXTURE_POSITIONS[2]-half, ChunkZQuadCounter.TEXTURE_POSITIONS[3]-half, 0, 1);
		ChunkZQuadCounter.temp3.set(ChunkZQuadCounter.TEXTURE_POSITIONS[4]-half, ChunkZQuadCounter.TEXTURE_POSITIONS[5]-half, 0, 1);
		ChunkZQuadCounter.temp4.set(ChunkZQuadCounter.TEXTURE_POSITIONS[6]-half, ChunkZQuadCounter.TEXTURE_POSITIONS[7]-half, 0, 1);
		if(r==1){
			Matrix4f.transform(ChunkZQuadCounter.mat1, ChunkZQuadCounter.temp1, ChunkZQuadCounter.temp1);
			Matrix4f.transform(ChunkZQuadCounter.mat1, ChunkZQuadCounter.temp2, ChunkZQuadCounter.temp2);
			Matrix4f.transform(ChunkZQuadCounter.mat1, ChunkZQuadCounter.temp3, ChunkZQuadCounter.temp3);
			Matrix4f.transform(ChunkZQuadCounter.mat1, ChunkZQuadCounter.temp4, ChunkZQuadCounter.temp4);
		}else if(r==2){
			Matrix4f.transform(ChunkZQuadCounter.mat2, ChunkZQuadCounter.temp1, ChunkZQuadCounter.temp1);
			Matrix4f.transform(ChunkZQuadCounter.mat2, ChunkZQuadCounter.temp2, ChunkZQuadCounter.temp2);
			Matrix4f.transform(ChunkZQuadCounter.mat2, ChunkZQuadCounter.temp3, ChunkZQuadCounter.temp3);
			Matrix4f.transform(ChunkZQuadCounter.mat2, ChunkZQuadCounter.temp4, ChunkZQuadCounter.temp4);
		}else{
			Matrix4f.transform(ChunkZQuadCounter.mat3, ChunkZQuadCounter.temp1, ChunkZQuadCounter.temp1);
			Matrix4f.transform(ChunkZQuadCounter.mat3, ChunkZQuadCounter.temp2, ChunkZQuadCounter.temp2);
			Matrix4f.transform(ChunkZQuadCounter.mat3, ChunkZQuadCounter.temp3, ChunkZQuadCounter.temp3);
			Matrix4f.transform(ChunkZQuadCounter.mat3, ChunkZQuadCounter.temp4, ChunkZQuadCounter.temp4);
		}
		ChunkZQuadCounter.TEXTURE_POSITIONS[0] = ChunkZQuadCounter.temp1.x+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[1] = ChunkZQuadCounter.temp1.y+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[2] = ChunkZQuadCounter.temp2.x+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[3] = ChunkZQuadCounter.temp2.y+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[4] = ChunkZQuadCounter.temp3.x+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[5] = ChunkZQuadCounter.temp3.y+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[6] = ChunkZQuadCounter.temp4.x+half;
		ChunkZQuadCounter.TEXTURE_POSITIONS[7] = ChunkZQuadCounter.temp4.y+half;
	}
	public void setup(int startX, int startY, int startZ, int z, int side, QuadBatch batch, int r, float red, float green, float blue){
		this.z      =      z;
		this.side   =   side;
		this.batch  =  batch;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r      =      r;
		ChunkZQuadCounter.COLORS[0] = red;
		ChunkZQuadCounter.COLORS[1] = green;
		ChunkZQuadCounter.COLORS[2] = blue;
	}
}