package com.wraithavens.conquest.SinglePlayer.Heightmap;

class QuadTree{
	final int x;
	final int z;
	final int size;
	final QuadTree[] children;
	final QuadTree parent;
	QuadTree(int x, int z, int size, QuadTree parent){
		this.x = x;
		this.z = z;
		this.size = size;
		this.parent = parent;
		children = new QuadTree[4];
	}
	QuadTree addChild(int index){
		if(children[index]!=null)
			return children[index];
		int nx = x;
		int nz = z;
		int s = size/2;
		if(index==1)
			nx += s;
		if(index==2)
			nz += s;
		if(index==3){
			nx += s;
			nz += s;
		}
		children[index] = new QuadTree(nx, nz, s, this);
		return children[index];
	}
	void clearChildren(){
		children[0] = null;
		children[1] = null;
		children[2] = null;
		children[3] = null;
	}
	int indexOf(QuadTree t){
		for(int i = 0; i<4; i++)
			if(children[i]==t)
				return i;
		return -1;
	}
}
