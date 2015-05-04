package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;

public class InfiniteWorld implements ChunkStorage{
	private final ArrayList<VoxelChunk> chunks = new ArrayList();
	public VoxelChunk getChunk(int x, int y, int z){
		VoxelChunk c;
		for(int i = 0; i<chunks.size(); i++){
			c=chunks.get(i);
			if(c.chunkX==x&&c.chunkY==y&&c.chunkZ==z)return c;
		}
		return null;
	}
	public void addChunk(VoxelChunk chunk){ chunks.add(chunk); }
	public void removeChunk(VoxelChunk chunk){ chunks.remove(chunk); }
	public int getChunkCount(){ return chunks.size(); }
	public VoxelChunk getChunk(int index){ return chunks.get(index); }
}