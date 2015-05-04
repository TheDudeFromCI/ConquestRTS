package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class BlockShape{
	private boolean showsXUp, showsXDown, showsYUp, showsYDown, showsZUp, showsZDown;
	private static final int X_OFFSET = 1;
	private static final int Y_OFFSET = 64;
	private static final int Z_OFFSET = 8;
	public abstract boolean fullSide(int side);
	public boolean getBlock(int x, int y, int z){ return getBlocks()[x*X_OFFSET+y*Y_OFFSET+z*Z_OFFSET]; }
	protected abstract boolean[] getBlocks();
}