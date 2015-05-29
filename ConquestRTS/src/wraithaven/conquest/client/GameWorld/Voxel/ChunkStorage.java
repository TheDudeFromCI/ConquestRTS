package wraithaven.conquest.client.GameWorld.Voxel;

public interface ChunkStorage{
	public void addChunk(Chunk chunk);
	public Chunk getChunk(int index);
	public Chunk getChunk(int x, int y, int z);
	public int getChunkCount();
	public void removeChunk(Chunk chunk);
}