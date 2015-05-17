package wraithaven.conquest.client.GameWorld.Voxel;

public abstract class BlockShape{
	private static final int X_OFFSET = 1;
	private static final int Z_OFFSET = 8;
	private static final int Y_OFFSET = 64;
	public boolean fullSide(int side, BlockRotation rot){ return hasFullSide(rot.rotateSide(side)); }
	private static final int[] SUB_BLOCK_POS = new int[3];
	public int hasNeighbor(int x, int y, int z, int side, BlockRotation rot){
		if(side==0)return x==7?-1:getBlock(x+1, y, z, rot)?1:0;
		if(side==1)return x==0?-1:getBlock(x-1, y, z, rot)?1:0;
		if(side==2)return y==7?-1:getBlock(x, y+1, z, rot)?1:0;
		if(side==3)return y==0?-1:getBlock(x, y-1, z, rot)?1:0;
		if(side==4)return z==7?-1:getBlock(x, y, z+1, rot)?1:0;
		if(side==5)return z==0?-1:getBlock(x, y, z-1, rot)?1:0;
		return -1;
	}
	protected abstract boolean[] getBlocks();
	public boolean getBlock(int x, int y, int z, BlockRotation rot){
		SUB_BLOCK_POS[0]=x;
		SUB_BLOCK_POS[1]=y;
		SUB_BLOCK_POS[2]=z;
		rot.rotate(SUB_BLOCK_POS);
		return getBlocks()[getIndex(SUB_BLOCK_POS[0], SUB_BLOCK_POS[1], SUB_BLOCK_POS[2])];
	}
	protected abstract boolean hasFullSide(int side);
	public static int getIndex(int x, int y, int z){ return x*X_OFFSET+y*Y_OFFSET+z*Z_OFFSET;  }
}