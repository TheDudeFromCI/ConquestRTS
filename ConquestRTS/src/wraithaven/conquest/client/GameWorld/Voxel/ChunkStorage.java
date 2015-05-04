package wraithaven.conquest.client.GameWorld.Voxel;

public interface ChunkStorage{
	public VoxelChunk getChunk(int x, int y, int z);
	public void addChunk(VoxelChunk chunk);
	public void removeChunk(VoxelChunk chunk);
	public int getChunkCount();
	public VoxelChunk getChunk(int index);
}