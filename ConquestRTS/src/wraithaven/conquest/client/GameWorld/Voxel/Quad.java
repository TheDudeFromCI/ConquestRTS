package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;

public class Quad{
	                               //          3                     8                12
	public final FloatBuffer data; // Colors (0-2), Texture Points (3-10), Vertices (11-22);
	public final int side;
	public Quad(float[] points, float[] colors, float[] texturePositions, int side){
		data = FloatBuffer.allocate(23);
		data.put(colors);
		data.put(texturePositions);
		data.put(points);
		this.side = side;
	}
}