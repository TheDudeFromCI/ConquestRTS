package com.wraithavens.conquest.SinglePlayer.Blocks;

public class VoxelChunk{
	private VoxelChunk[] children;
	protected final int x;
	protected final int y;
	protected final int z;
	protected final int size;
	protected boolean isEmpty;
	public VoxelChunk(int x, int y, int z, int size){
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
	}
	public void dispose(){
		if(children==null)
			return;
		for(int i = 0; i<8; i++)
			children[i].dispose();
	}
	public VoxelChunk getChild(int index){
		return children[index];
	}
	public int getSize(){
		return size;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getZ(){
		return z;
	}
	public boolean hasChildrenVoxels(){
		return children!=null;
	}
	public void render(ChunkRenderTester tester){
		// ---
		// Make sure these areas can actually render.
		// ---
		if(children==null)
			return;
		if(isEmpty)
			return;
		if(!tester.canRender(this))
			return;
		// ---
		// Now render all children.
		// ---
		for(int i = 0; i<8; i++)
			children[i].render(tester);
	}
}
