package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;

public class Quad{
	int rotation;
	boolean scale;
	boolean centerPoint;
	public final FloatBuffer data;  //Colors (0-14), Texture Points (15-24), Vertices (25-39);
	private static final float[] ROTATION_0 = {0, 0, 0, 1, 1, 1, 1, 0, 0.5f, 0.5f};
	private static final float[] ROTATION_1 = {0, 1, 1, 1, 1, 0, 0, 0, 0.5f, 0.5f};
	private static final float[] ROTATION_2 = {1, 1, 1, 0, 0, 0, 0, 1, 0.5f, 0.5f};
	private static final float[] ROTATION_3 = {1, 0, 0, 0, 0, 1, 1, 1, 0.5f, 0.5f};
	Quad(float[] points, float[] colors, int rotation, boolean scale, float[] texturePositions){
		data=FloatBuffer.allocate(40);
		data.put(colors);
		if(rotation==0)feedTexturePositions(data, ROTATION_0, texturePositions);
		if(rotation==1)feedTexturePositions(data, ROTATION_1, texturePositions);
		if(rotation==2)feedTexturePositions(data, ROTATION_2, texturePositions);
		if(rotation==3)feedTexturePositions(data, ROTATION_3, texturePositions);
		if(!scale)for(int i = 25; i<=39; i++)data.put(i, points[i-25]/8f);
		else data.put(points);
		this.rotation=rotation;
		this.scale=scale;
	}
	void shift(float x, float y, float z){
		data.put(25, data.get(25)+x);
		data.put(26, data.get(26)+y);
		data.put(27, data.get(27)+z);
		data.put(28, data.get(28)+x);
		data.put(29, data.get(29)+y);
		data.put(30, data.get(30)+z);
		data.put(31, data.get(31)+x);
		data.put(32, data.get(32)+y);
		data.put(33, data.get(33)+z);
		data.put(34, data.get(34)+x);
		data.put(35, data.get(35)+y);
		data.put(36, data.get(36)+z);
		data.put(37, data.get(37)+x);
		data.put(38, data.get(38)+y);
		data.put(39, data.get(39)+z);
	}
	private static void feedTexturePositions(FloatBuffer data, float[] rotations, float[] texturePositions){
		data.put(rotations[0]*texturePositions[1]+texturePositions[0]);
		data.put(rotations[1]*texturePositions[3]+texturePositions[2]);
		data.put(rotations[2]*texturePositions[1]+texturePositions[0]);
		data.put(rotations[3]*texturePositions[3]+texturePositions[2]);
		data.put(rotations[4]*texturePositions[1]+texturePositions[0]);
		data.put(rotations[5]*texturePositions[3]+texturePositions[2]);
		data.put(rotations[6]*texturePositions[1]+texturePositions[0]);
		data.put(rotations[7]*texturePositions[3]+texturePositions[2]);
		data.put(rotations[8]*texturePositions[1]+texturePositions[0]);
		data.put(rotations[9]*texturePositions[3]+texturePositions[2]);
	}
}