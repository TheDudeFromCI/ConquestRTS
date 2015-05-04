package wraithaven.conquest.client.GameWorld.Voxel;

public interface ChunkStorage{
	public Chunk getChunk(int x, int y, int z);
	public void addChunk(Chunk chunk);
	public void removeChunk(Chunk chunk);
	public int getChunkCount();
	public Chunk getChunk(int index);
}