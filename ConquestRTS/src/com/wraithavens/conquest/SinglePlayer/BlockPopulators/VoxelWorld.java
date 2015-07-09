package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.util.ArrayList;

class VoxelWorld{
	private static boolean collidesWith(int x, int y, int z, int size, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize){
		int nx = x+size;
		int ny = y+size;
		int nz = z+size;
		if((x>=boxX&&x<boxXSize)
				||(nx>=boxX&&nx<boxXSize)
				||(boxX>=x&&boxX<nx)
				||(boxXSize>=x&&boxXSize<nx)){
			if((y>=boxY&&y<boxYSize)
					||(ny>=boxY&&ny<boxYSize)
					||(boxY>=y&&boxY<ny)
					||(boxYSize>=y&&boxYSize<ny)){
				if((z>=boxZ&&z<boxZSize)
						||(nz>=boxZ&&nz<boxZSize)
						||(boxZ>=z&&boxZ<nz)
						||(boxZSize>=z&&boxZSize<nz)){
					return true;
				}
			}
		}
		return false;
	}
	private static boolean isInBox(int x, int y, int z, int size, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize){
		int nx = x+size;
		int ny = y+size;
		int nz = z+size;
		return VoxelWorld.isPointInBox(x, y, z, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize)
				&&VoxelWorld.isPointInBox(nx, ny, nz, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize);
	}
	private static boolean isPointInBox(int x, int y, int z, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize){
		return x+0.1f>boxX
				&&y+0.1f>boxY
				&&z+0.1f>boxZ
				&&x-0.1f<boxX+boxXSize
				&&y-0.1f<boxY+boxYSize
				&&z-0.1f<boxZ+boxZSize;
	}
	private static int touchesBox(int x, int y, int z, int size, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize){
		if(VoxelWorld.isInBox(x, y, z, size, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize))return 2;
		if(VoxelWorld.collidesWith(x, y, z, size, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize))return 1;
		return 0;
	}
	private static boolean isInVoxel(Voxel vox, int x, int y, int z){
		return VoxelWorld.isInBox(x, y, z, 1, vox.x, vox.y, vox.z, vox.size, vox.size, vox.size);
	}
	final ArrayList<Voxel> voxels = new ArrayList();
	private final int worldBits;
	private final int voxelSize;
	VoxelWorld(int worldBits){
		this.worldBits = worldBits;
		voxelSize = (int)Math.pow(2, worldBits);
	}
	private Voxel getVoxel(int x, int y, int z, boolean load){
		for(int i = 0; i<voxels.size(); i++)
			if(voxels.get(i).x==x
			&&voxels.get(i).y==y
			&&voxels.get(i).z==z)return voxels.get(i);
		if(load)return loadVoxel(x, y, z);
		return null;
	}
	private Voxel loadVoxel(int x, int y, int z){
		Voxel vox = new Voxel(x, y, z, voxelSize, Voxel.DEFAULT);
		voxels.add(vox);
		return vox;
	}
	void fillArea(int boxX, int boxY, int boxZ, int xSize, int ySize, int zSize, EmptyChunk data){
		int startX = (boxX>>worldBits);
		int startY = (boxY>>worldBits);
		int startZ = (boxZ>>worldBits);
		int endX = (boxX+xSize)>>worldBits;
		int endY = (boxY+ySize)>>worldBits;
		int endZ = (boxZ+zSize)>>worldBits;
		if((boxX+xSize)%voxelSize==0)endX--;
		if((boxY+ySize)%voxelSize==0)endY--;
		if((boxZ+zSize)%voxelSize==0)endZ--;
		int x, y, z;
		for(x = startX; x<=endX; x++)
			for(y = startY; y<=endY; y++)
				for(z = startZ; z<=endZ; z++)
					processVoxel(getVoxel(x*voxelSize, y*voxelSize, z*voxelSize, true), boxX, boxY, boxZ, xSize, ySize, zSize, data);
	}
	private void processVoxel(Voxel voxel, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize, EmptyChunk data){
		breakDown(voxel, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize, data);
		voxel.simplify();
	}
	private void breakDown(Voxel voxel, int boxX, int boxY, int boxZ, int boxXSize, int boxYSize, int boxZSize, EmptyChunk data){
		int coverage = VoxelWorld.touchesBox(voxel.x, voxel.y, voxel.z, voxel.size, boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize);
		if(coverage==0)return;
		if(coverage==2){
			voxel.makeSolid(data);
			return;
		}
		if(voxel.size==1)return;
		voxel.breakDown();
		for(int i = 0; i<8; i++)
			breakDown(voxel.getVoxel(i), boxX, boxY, boxZ, boxXSize, boxYSize, boxZSize, data);
	}
	Voxel getSubVoxelAt(int x, int y, int z, boolean load){
		Voxel parent = getVoxel((x>>worldBits)*voxelSize, (y>>worldBits)*voxelSize, (z>>worldBits)*voxelSize, load);
		return searchFor(parent, x, y, z);
	}
	private Voxel searchFor(Voxel vox, int x, int y, int z){
		if(vox==null)return null;
		if(vox.isSolid())return vox;
		for(int i = 0; i<8; i++)
			if(VoxelWorld.isInVoxel(vox.getVoxel(i), x, y, z))return searchFor(vox.getVoxel(i), x, y, z);
		return null;
	}
}