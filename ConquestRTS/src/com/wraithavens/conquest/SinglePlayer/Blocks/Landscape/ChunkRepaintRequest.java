package com.wraithavens.conquest.SinglePlayer.Blocks.Landscape;

import com.wraithavens.conquest.SinglePlayer.Blocks.BlockMesher.MeshRenderer;

public class ChunkRepaintRequest{
	private final int x;
	private final int y;
	private final int z;
	private final MeshRenderer newMesh;
	public ChunkRepaintRequest(int x, int y, int z, MeshRenderer newMesh){
		this.x = x;
		this.y = y;
		this.z = z;
		this.newMesh = newMesh;
	}
	MeshRenderer getNewMesh(){
		return newMesh;
	}
	int getX(){
		return x;
	}
	int getY(){
		return y;
	}
	int getZ(){
		return z;
	}
}
