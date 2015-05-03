package wraithaven.conquest.client.GameWorld.Voxel;

public interface VoxelWorldListener{
	public boolean isChunkVisible(VoxelChunk chunk);
	public void loadChunk(VoxelChunk chunk);
	public void unloadChunk(VoxelChunk chunk);
}