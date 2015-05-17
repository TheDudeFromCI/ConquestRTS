package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class Cube{
	public boolean renderXUp, renderXDown, renderYUp, renderYDown, renderZUp, renderZDown;
	public CubeTextures textures;
	private static final float[] X_UP_QUAD = {
		1, 1, 1,
		1, 0, 1,
		1, 0, 0,
		1, 1, 0,
		1, 0.5f, 0.5f
	};
	private static final float[] X_DOWN_QUAD = {
		0, 0, 0,
		0, 0, 1,
		0, 1, 1,
		0, 1, 0,
		0, 0.5f, 0.5f
	};
	private static final float[] Y_UP_QUAD = {
		0, 1, 0,
		0, 1, 1,
		1, 1, 1,
		1, 1, 0,
		0.5f, 1, 0.5f
	};
	private static final float[] Y_DOWN_QUAD = {
		1, 0, 1,
		0, 0, 1,
		0, 0, 0,
		1, 0, 0,
		0.5f, 0, 0.5f
	};
	private static final float[] Z_UP_QUAD = {
		1, 1, 1,
		0, 1, 1,
		0, 0, 1,
		1, 0, 1,
		0.5f, 0.5f, 1
	};
	private static final float[] Z_DOWN_QUAD = {
		0, 0, 0,
		0, 1, 0,
		1, 1, 0,
		1, 0, 0,
		0.5f, 0.5f, 0
	};
	public static final int X_UP_SIDE = 0;
	public static final int X_DOWN_SIDE = 1;
	public static final int Y_UP_SIDE = 2;
	public static final int Y_DOWN_SIDE = 3;
	public static final int Z_UP_SIDE = 4;
	public static final int Z_DOWN_SIDE = 5;
	public static Quad generateQuad(int side, float x, float y, float z, int r, float[] colors, float scale, float[] texturePositions){
		Quad q = null;
		if(side==0)q=new Quad(X_UP_QUAD, colors, r, scale, texturePositions, side);
		if(side==1)q=new Quad(X_DOWN_QUAD, colors, r, scale, texturePositions, side);
		if(side==2)q=new Quad(Y_UP_QUAD, colors, r, scale, texturePositions, side);
		if(side==3)q=new Quad(Y_DOWN_QUAD, colors, r, scale, texturePositions, side);
		if(side==4)q=new Quad(Z_UP_QUAD, colors, r, scale, texturePositions, side);
		if(side==5)q=new Quad(Z_DOWN_QUAD, colors, r, scale, texturePositions, side);
		if(q!=null)q.shift(x, y, z);
		return q;
	}
	public static float getVertex(int side, int index){
		if(side==0)return X_UP_QUAD[index];
		if(side==1)return X_DOWN_QUAD[index];
		if(side==2)return Y_UP_QUAD[index];
		if(side==3)return Y_DOWN_QUAD[index];
		if(side==4)return Z_UP_QUAD[index];
		if(side==5)return Z_DOWN_QUAD[index];
		return 0;
	}
}