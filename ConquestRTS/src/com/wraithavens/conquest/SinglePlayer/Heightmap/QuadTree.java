package com.wraithavens.conquest.SinglePlayer.Heightmap;

class QuadTree{
	final int x;
	final int z;
	final int size;
	final QuadTree[] children;
	QuadTree(int x, int z, int size){
		this.x = x;
		this.z = z;
		this.size = size;
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
		children[index] = new QuadTree(nx, nz, s);
		return children[index];
	}
}
