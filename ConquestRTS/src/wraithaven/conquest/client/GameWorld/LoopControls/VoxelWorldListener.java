package wraithaven.conquest.client.GameWorld.LoopControls;

import wraithaven.conquest.client.GameWorld.Voxel.VoxelChunk;

public interface VoxelWorldListener{
	public boolean isChunkVisible(VoxelChunk chunk);
	public void loadChunk(VoxelChunk chunk);
	public void unloadChunk(VoxelChunk chunk);
}