package wraithaven.conquest.client.GameWorld.LoopControls;

import wraithaven.conquest.client.GameWorld.Voxel.Chunk;

public interface VoxelWorldListener{
	public boolean isChunkVisible(Chunk chunk);
	public void loadChunk(Chunk chunk);
	public void unloadChunk(Chunk chunk);
}