package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;

public class BuildCreatorWorld implements VoxelWorldListener{
	private VoxelWorld world;
	private Camera camera;
	public void setup(VoxelWorld world, Camera camera){
		this.world=world;
		this.camera=camera;
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return !chunk.isHidden()&&camera.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, 16); }
	public void loadChunk(VoxelChunk chunk){}
	public void unloadChunk(VoxelChunk chunk){}
}