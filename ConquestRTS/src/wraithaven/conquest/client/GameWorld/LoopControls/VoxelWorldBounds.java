package wraithaven.conquest.client.GameWorld.LoopControls;

import wraithaven.conquest.client.GameWorld.Voxel.Chunk;

public class VoxelWorldBounds{
	public final int startX, startY, startZ;
	public final int endX, endY, endZ;
	public final int chunkStartX, chunkStartY, chunkStartZ;
	public final int chunkEndX, chunkEndY, chunkEndZ;
	public VoxelWorldBounds(int startX, int startY, int startZ, int endX, int endY, int endZ){
		this.startX=startX;
		this.startY=startY;
		this.startZ=startZ;
		this.endX=endX;
		this.endY=endY;
		this.endZ=endZ;
		chunkStartX=startX>>Chunk.CHUNK_BITS;
		chunkStartY=startY>>Chunk.CHUNK_BITS;
		chunkStartZ=startZ>>Chunk.CHUNK_BITS;
		chunkEndX=endX>>Chunk.CHUNK_BITS;
		chunkEndY=endY>>Chunk.CHUNK_BITS;
		chunkEndZ=endZ>>Chunk.CHUNK_BITS;
	}
}