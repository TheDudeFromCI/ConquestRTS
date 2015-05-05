package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class BlockShape{
	private static final int X_OFFSET = 1;
	private static final int Z_OFFSET = 8;
	private static final int Y_OFFSET = 64;
	public abstract boolean fullSide(int side);
	public boolean getBlock(int x, int y, int z){ return getBlocks()[getIndex(x, y, z)]; }
	public boolean getBlock(int index){ return getBlocks()[index]; }
	public int hasNeighbor(int x, int y, int z, int side){
		if(side==0)return x==7?-1:getBlock(x+1, y, z)?1:0;
		if(side==1)return x==0?-1:getBlock(x-1, y, z)?1:0;
		if(side==2)return y==7?-1:getBlock(x, y+1, z)?1:0;
		if(side==3)return y==0?-1:getBlock(x, y-1, z)?1:0;
		if(side==4)return z==7?-1:getBlock(x, y, z+1)?1:0;
		if(side==5)return z==0?-1:getBlock(x, y, z-1)?1:0;
		return -1;
	}
	protected abstract boolean[] getBlocks();
	public static int getIndex(int x, int y, int z){ return x*X_OFFSET+y*Y_OFFSET+z*Z_OFFSET;  }
}