package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;

public class InfiniteWorld implements ChunkStorage{
	private final ArrayList<Chunk> chunks = new ArrayList();
	public Chunk getChunk(int x, int y, int z){
		Chunk c;
		for(int i = 0; i<chunks.size(); i++){
			c=chunks.get(i);
			if(c.chunkX==x&&c.chunkY==y&&c.chunkZ==z)return c;
		}
		return null;
	}
	public void addChunk(Chunk chunk){ chunks.add(chunk); }
	public void removeChunk(Chunk chunk){ chunks.remove(chunk); }
	public int getChunkCount(){ return chunks.size(); }
	public Chunk getChunk(int index){ return chunks.get(index); }
}