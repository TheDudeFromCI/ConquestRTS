package wraithaven.conquest.client.GameWorld.Voxel;

import java.nio.FloatBuffer;

public interface BlockType{
	public Texture getTexture(int side);
	public int getRotation(int side);
	public boolean setupShadows(FloatBuffer dataArray, int side, int x, int y, int z);
}