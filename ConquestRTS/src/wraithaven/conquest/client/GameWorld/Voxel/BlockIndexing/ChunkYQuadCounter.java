package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import wraithaven.conquest.client.GameWorld.LoopControls.Matrix4f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector3f;
import wraithaven.conquest.client.GameWorld.LoopControls.Vector4f;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.Quad;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;

public class ChunkYQuadCounter implements QuadCounter{
	private static final float[] COLORS = {1, 1, 1};
	private static Matrix4f mat1 = new Matrix4f();
	private static Matrix4f mat2 = new Matrix4f();
	private static Matrix4f mat3 = new Matrix4f();
	private static final float[] QUAD_POINTS = new float[12];
	private static Vector4f temp1 = new Vector4f();
	private static Vector4f temp2 = new Vector4f();
	private static Vector4f temp3 = new Vector4f();
	private static Vector4f temp4 = new Vector4f();
	private static final float[] TEXTURE_POSITIONS = new float[8];
	static{
		mat1.rotate((float)Math.toRadians(90), new Vector3f(0, 0, 1));
		mat2.rotate((float)Math.toRadians(180), new Vector3f(0, 0, 1));
		mat3.rotate((float)Math.toRadians(270), new Vector3f(0, 0, 1));
	}
	private QuadBatch batch;
	private int m;
	private int y,
			side,
			startX,
			startY,
			startZ,
			r;
	public void addQuad(int x, int z, int w, int h){
		float smallX = x/8f+startX;
		float smallY = y/8f+startY;
		float smallZ = z/8f+startZ;
		float bigX = smallX+w/8f;
		float bigY = smallY+1/8f;
		float bigZ = smallZ+h/8f;
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
			m = 0;
			next(z, x);
			next(z+h, x);
			next(z+h, x+w);
			next(z, x+w);
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
			m = 4;
			next(-(z+h), x+w);
			next(-(z+h), x);
			next(-z, x);
			next(-z, x+w);
		}
		rot();
		batch.addQuad(new Quad(QUAD_POINTS, COLORS, TEXTURE_POSITIONS, side));
	}
	private void next(float x, float y){
		TEXTURE_POSITIONS[m] = y/8f;
		TEXTURE_POSITIONS[m+1] = x/8f;
		m = (m+2)%8;
	}
	private void rot(){
		if(r==0) return;
		float half = Chunk.BLOCKS_PER_CHUNK/2f;
		temp1.set(TEXTURE_POSITIONS[0]-half, TEXTURE_POSITIONS[1]-half, 0, 1);
		temp2.set(TEXTURE_POSITIONS[2]-half, TEXTURE_POSITIONS[3]-half, 0, 1);
		temp3.set(TEXTURE_POSITIONS[4]-half, TEXTURE_POSITIONS[5]-half, 0, 1);
		temp4.set(TEXTURE_POSITIONS[6]-half, TEXTURE_POSITIONS[7]-half, 0, 1);
		if(r==1){
			Matrix4f.transform(mat1, temp1, temp1);
			Matrix4f.transform(mat1, temp2, temp2);
			Matrix4f.transform(mat1, temp3, temp3);
			Matrix4f.transform(mat1, temp4, temp4);
		}else if(r==2){
			Matrix4f.transform(mat2, temp1, temp1);
			Matrix4f.transform(mat2, temp2, temp2);
			Matrix4f.transform(mat2, temp3, temp3);
			Matrix4f.transform(mat2, temp4, temp4);
		}else{
			Matrix4f.transform(mat3, temp1, temp1);
			Matrix4f.transform(mat3, temp2, temp2);
			Matrix4f.transform(mat3, temp3, temp3);
			Matrix4f.transform(mat3, temp4, temp4);
		}
		TEXTURE_POSITIONS[0] = temp1.x+half;
		TEXTURE_POSITIONS[1] = temp1.y+half;
		TEXTURE_POSITIONS[2] = temp2.x+half;
		TEXTURE_POSITIONS[3] = temp2.y+half;
		TEXTURE_POSITIONS[4] = temp3.x+half;
		TEXTURE_POSITIONS[5] = temp3.y+half;
		TEXTURE_POSITIONS[6] = temp4.x+half;
		TEXTURE_POSITIONS[7] = temp4.y+half;
	}
	public void setup(int startX, int startY, int startZ, int y, int side, QuadBatch batch, int r, float red, float green, float blue){
		this.y = y;
		this.side = side;
		this.batch = batch;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
		this.r = r;
		COLORS[0] = red;
		COLORS[1] = green;
		COLORS[2] = blue;
	}
}