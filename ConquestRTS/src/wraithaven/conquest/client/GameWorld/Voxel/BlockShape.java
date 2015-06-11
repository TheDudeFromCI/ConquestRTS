package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class BlockShape{
	private static final int[] CORD_TEMP = new int[3];
	private static final int X_OFFSET = 1;
	private static final int Y_OFFSET = 64;
	private static final int Z_OFFSET = 8;
	public static int getIndex(int x, int y, int z){
		return x*BlockShape.X_OFFSET+y*BlockShape.Y_OFFSET+z*BlockShape.Z_OFFSET;
	}
	public boolean getBlock(int x, int y, int z, BlockRotation rot){
		BlockShape.CORD_TEMP[0] = x;
		BlockShape.CORD_TEMP[1] = y;
		BlockShape.CORD_TEMP[2] = z;
		rot.rotate(BlockShape.CORD_TEMP);
		return getBlocks()[BlockShape.getIndex(BlockShape.CORD_TEMP[0], BlockShape.CORD_TEMP[1], BlockShape.CORD_TEMP[2])];
	}
	protected abstract boolean[] getBlocks();
}