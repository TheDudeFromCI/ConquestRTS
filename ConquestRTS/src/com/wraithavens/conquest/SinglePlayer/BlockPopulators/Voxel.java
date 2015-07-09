package com.wraithavens.conquest.SinglePlayer.BlockPopulators;


/*
 * TODO Switch these voxels to be custom for chunks.
 * TODO Made checks for is voxel contains blocks or not. Voxels that do not contain any blocks should not
 * be rendered.
 * Also use voxels to render LOD'd chunks.
 */
class Voxel{
	final static EmptyChunk DEFAULT = new EmptyChunk(){};
	private EmptyChunk state;
	private Voxel[] branches;
	final int x;
	final int y;
	final int z;
	final int size;
	public Voxel(int x, int y, int z, int size, EmptyChunk state){
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
	}
	void makeSolid(EmptyChunk state){
		this.state = state;
		branches = null;
	}
	void breakDown(){
		if(size==1)throw new RuntimeException("Cannot break down any further!");
		if(branches==null){
			branches = new Voxel[8];
			int s = size/2;
			branches[0] = new Voxel(  x,   y,   z,   s, state);
			branches[1] = new Voxel(  x,   y, z+s,   s, state);
			branches[2] = new Voxel(  x, y+s,   z,   s, state);
			branches[3] = new Voxel(  x, y+s, z+s,   s, state);
			branches[4] = new Voxel(x+s,   y,   z,   s, state);
			branches[5] = new Voxel(x+s,   y,   z+s, s, state);
			branches[6] = new Voxel(x+s, y+s,   z,   s, state);
			branches[7] = new Voxel(x+s, y+s,   z+s, s, state);
		}
	}
	Voxel getVoxel(int index){
		if(branches==null)return null;
		return branches[index];
	}
	boolean isSolid(){
		return branches==null;
	}
	EmptyChunk getState(){
		return state;
	}
	EmptyChunk simplify(){
		if(branches!=null){
			int similar = 0;
			EmptyChunk base = branches[0].simplify();
			for(int i = 1; i<8; i++)
				if(branches[i].simplify()==base)similar++;
			if(base!=null
					&&similar==7){
				state = base;
				branches = null;
			}else return null;
		}
		return state;
	}
}