package wraithaven.conquest.client.GameWorld.Voxel;

public interface BlockType{
	public Texture getTexture(int side);
	public int getRotation(int side);
	public boolean setupShadows(float[] colors, int side, int x, int y, int z);
}