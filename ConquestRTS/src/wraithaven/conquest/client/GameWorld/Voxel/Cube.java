package wraithaven.conquest.client.GameWorld.Voxel;

public class Cube{
	public static final float[] X_UP_QUAD = {
		1, 1, 1,
		1, 0, 1,
		1, 0, 0,
		1, 1, 0,
	};
	public static final float[] X_DOWN_QUAD = {
		0, 0, 0,
		0, 0, 1,
		0, 1, 1,
		0, 1, 0,
	};
	public static final float[] Y_UP_QUAD = {
		0, 1, 0,
		0, 1, 1,
		1, 1, 1,
		1, 1, 0,
	};
	public static final float[] Y_DOWN_QUAD = {
		1, 0, 1,
		0, 0, 1,
		0, 0, 0,
		1, 0, 0,
	};
	public static final float[] Z_UP_QUAD = {
		1, 1, 1,
		0, 1, 1,
		0, 0, 1,
		1, 0, 1,
	};
	public static final float[] Z_DOWN_QUAD = {
		0, 0, 0,
		0, 1, 0,
		1, 1, 0,
		1, 0, 0,
	};
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