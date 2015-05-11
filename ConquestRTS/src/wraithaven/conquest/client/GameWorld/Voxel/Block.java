package wraithaven.conquest.client.GameWorld.Voxel;

public class Block{
	public final int x, y, z;
	private boolean hidden;
	final Quad[] quads = new Quad[6];
	public final Chunk chunk;
	public final BlockType type;
	protected static final float[] WHITE_COLORS = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	protected static final float[] TEXTURE_POSITIONS = {0, 1, 0, 1};
	Block(Chunk chunk, int x, int y, int z, BlockType type){
		this.x=x;
		this.y=y;
		this.z=z;
		this.chunk=chunk;
		this.type=type;
	}
	protected void setHidden(boolean hidden){
		if(this.hidden==hidden)return;
		this.hidden=hidden;
		if(hidden)chunk.addHidden();
		else chunk.removeHidden();
	}
	void showSide(int side, boolean show){
		if(isSideShown(side)==show)return;
		updateSideVisibility(side, show);
		if(isFullyHidden())setHidden(true);
		else setHidden(false);
	}
	private void updateSideVisibility(int side, boolean show){
		if(show)quads[side]=Cube.generateQuad(side, x, y, z, type.getRotation(side), WHITE_COLORS, 1, TEXTURE_POSITIONS);
		else quads[side]=null;
	}
	public Block getTouchingBlock(int side){
		if(side==0)return chunk.world.getBlock(x+1, y, z, false);
		if(side==1)return chunk.world.getBlock(x-1, y, z, false);
		if(side==2)return chunk.world.getBlock(x, y+1, z, false);
		if(side==3)return chunk.world.getBlock(x, y-1, z, false);
		if(side==4)return chunk.world.getBlock(x, y, z+1, false);
		if(side==5)return chunk.world.getBlock(x, y, z-1, false);
		return null;
	}
	public Quad getQuad(int side){ return quads[side]; }
	public boolean isHidden(){ return hidden; }
	boolean isSideShown(int side){ return quads[side]!=null; }
	protected boolean isFullyHidden(){ return quads[0]!=null&&quads[1]!=null&&quads[2]!=null&&quads[3]!=null&&quads[4]!=null&&quads[5]!=null; }
}