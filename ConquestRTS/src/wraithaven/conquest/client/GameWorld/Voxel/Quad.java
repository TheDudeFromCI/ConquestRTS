package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;

public class Quad{
	int rotation;
	boolean scale;
	boolean centerPoint;
	final FloatBuffer data;  //Colors (0-14), Texture Points (15-22), Vertices (23-37);
	private static final float[] ROTATION_0 = {0, 0, 0, 1, 1, 1, 1, 0};
	private static final float[] ROTATION_1 = {0, 1, 1, 1, 1, 0, 0, 0};
	private static final float[] ROTATION_2 = {1, 1, 1, 0, 0, 0, 0, 1};
	private static final float[] ROTATION_3 = {1, 0, 0, 0, 0, 1, 1, 1};
	private static final float[] ROTATION_4 = {0, 0, 0, 8, 8, 8, 8, 0};
	private static final float[] ROTATION_5 = {0, 8, 8, 8, 8, 0, 0, 0};
	private static final float[] ROTATION_6 = {8, 8, 8, 0, 0, 0, 0, 8};
	private static final float[] ROTATION_7 = {8, 0, 0, 0, 0, 8, 8, 8};
	Quad(float[] points, float[] colors, int rotation, boolean scale){
		data=FloatBuffer.allocate(38);
		data.put(colors);
		if(rotation==0)data.put(scale?ROTATION_4:ROTATION_0);
		if(rotation==1)data.put(scale?ROTATION_5:ROTATION_1);
		if(rotation==2)data.put(scale?ROTATION_6:ROTATION_2);
		if(rotation==3)data.put(scale?ROTATION_7:ROTATION_3);
		if(!scale)for(int i = 23; i<=37; i++)data.put(i, points[i-23]/8f);
		else data.put(points);
		this.rotation=rotation;
		this.scale=scale;
	}
	void shift(float x, float y, float z){
		data.put(23, data.get(23)+x);
		data.put(24, data.get(24)+y);
		data.put(25, data.get(25)+z);
		data.put(26, data.get(26)+x);
		data.put(27, data.get(27)+y);
		data.put(28, data.get(28)+z);
		data.put(29, data.get(29)+x);
		data.put(30, data.get(30)+y);
		data.put(31, data.get(31)+z);
		data.put(32, data.get(32)+x);
		data.put(33, data.get(33)+y);
		data.put(34, data.get(34)+z);
		data.put(35, data.get(35)+x);
		data.put(36, data.get(36)+y);
		data.put(37, data.get(37)+z);
	}
	public void setRotation(int r){
		rotation=r;
		data.position(15);
		if(rotation==0)data.put(scale?ROTATION_4:ROTATION_0, 0, 8);
		if(rotation==1)data.put(scale?ROTATION_5:ROTATION_1, 0, 8);
		if(rotation==2)data.put(scale?ROTATION_6:ROTATION_2, 0, 8);
		if(rotation==3)data.put(scale?ROTATION_7:ROTATION_3, 0, 8);
	}
}