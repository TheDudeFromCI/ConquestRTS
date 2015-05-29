package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class BlockShape{
	private static final int[] CORD_TEMP = new int[3];
	private static final int X_OFFSET = 1;
	private static final int Y_OFFSET = 64;
	private static final int Z_OFFSET = 8;
	public static int getIndex(int x, int y, int z){
		return x*X_OFFSET+y*Y_OFFSET+z*Z_OFFSET;
	}
	public boolean getBlock(int x, int y, int z, BlockRotation rot){
		CORD_TEMP[0] = x;
		CORD_TEMP[1] = y;
		CORD_TEMP[2] = z;
		rot.rotate(CORD_TEMP);
		return getBlocks()[getIndex(CORD_TEMP[0], CORD_TEMP[1], CORD_TEMP[2])];
	}
	protected abstract boolean[] getBlocks();
}