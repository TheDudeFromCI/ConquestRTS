package wraithaven.conquest.client.GameWorld.Voxel;

public class Cube{
	public static final float[] X_UP_QUAD = {1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0};
	public static final float[] X_DOWN_QUAD = {0, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0};
	public static final float[] Y_UP_QUAD = {0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0};
	public static final float[] Y_DOWN_QUAD = {1, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0};
	public static final float[] Z_UP_QUAD = {1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1};
	public static final float[] Z_DOWN_QUAD = {0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0};
	public static float getVertex(int side, int index){
		if(side==0)return Cube.X_UP_QUAD[index];
		if(side==1)return Cube.X_DOWN_QUAD[index];
		if(side==2)return Cube.Y_UP_QUAD[index];
		if(side==3)return Cube.Y_DOWN_QUAD[index];
		if(side==4)return Cube.Z_UP_QUAD[index];
		if(side==5)return Cube.Z_DOWN_QUAD[index];
		return 0;
	}
}