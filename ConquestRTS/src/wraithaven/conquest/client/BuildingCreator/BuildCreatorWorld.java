package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;
import wraithaven.conquest.client.BlockTextures;
import wraithaven.conquest.client.BasicBlock;

public class BuildCreatorWorld implements VoxelWorldListener{
	private Camera camera;
	private BasicBlock basic;
	public void setup(Camera camera){
		this.camera=camera;
		basic=new BasicBlock(BlockTextures.grass1.getTextures());
	}
	public void loadChunk(VoxelChunk chunk){
		chunk.setBlock(chunk.startX+1, chunk.startY+1, chunk.startZ+1, basic);
		chunk.optimizeBlock(chunk.getBlock(chunk.startX+1, chunk.startY+1, chunk.startZ+1));
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return !chunk.isHidden()&&camera.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, 16); }
	public void unloadChunk(VoxelChunk chunk){}
}