package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;

public class BuildCreatorWorld implements VoxelWorldListener{
	private VoxelWorld world;
	public boolean isChunkVisible(VoxelChunk chunk){
		return false;
	}
	public void loadChunk(VoxelChunk chunk){
	}
	public void unloadChunk(VoxelChunk chunk){
	}
	public void setup(VoxelWorld world){ this.world=world; }
}