package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import com.wraithavens.conquest.SinglePlayer.ChunkXQuadCounter;
import com.wraithavens.conquest.SinglePlayer.ChunkYQuadCounter;
import com.wraithavens.conquest.SinglePlayer.ChunkZQuadCounter;
import com.wraithavens.conquest.SinglePlayer.Quad;
import com.wraithavens.conquest.SinglePlayer.QuadBatchHolder;
import com.wraithavens.conquest.SinglePlayer.QuadListener;
import com.wraithavens.conquest.SinglePlayer.QuadOptimizer;

class Chunk extends QuadBatchHolder implements EmptyChunk{
	private static final int getIndex(int x, int y, int z){
		return (x&Chunk.HIGH_BLOCK_COUNT)+(y&Chunk.HIGH_BLOCK_COUNT)*Chunk.BLOCKS_PER_CHUNK+(z&Chunk.HIGH_BLOCK_COUNT)*Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK;
	}
	static final int CHUNK_BITS = 5;
	private static final int BLOCKS_PER_CHUNK = (int)Math.pow(2, Chunk.CHUNK_BITS);
	private static final int TOTAL_BLOCKS = (int)Math.pow(Chunk.BLOCKS_PER_CHUNK, 3);
	private static final int HIGH_BLOCK_COUNT = Chunk.BLOCKS_PER_CHUNK-1;
	private static final boolean HIGH_QUALITY_QUAD_MESHING = true;
	private static final boolean[][] QUAD_LAYER = new boolean[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
	private static final int[][] QUAD_STORAGE = new int[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
	private static final int[][] QUAD_STORAGE_2 = new int[Chunk.BLOCKS_PER_CHUNK][Chunk.BLOCKS_PER_CHUNK];
	private static final ChunkXQuadCounter X_QUAD_COUNTER = new ChunkXQuadCounter(true);
	private static final ChunkYQuadCounter Y_QUAD_COUNTER = new ChunkYQuadCounter(true);
	private static final ChunkZQuadCounter Z_QUAD_COUNTER = new ChunkZQuadCounter(true);
	private static final BlockProperties PROPERTIES = new BlockProperties(Math.min(Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK, Block.values().length));
	private final byte[] blocks = new byte[Chunk.TOTAL_BLOCKS];
	private final QuadListener quadListener = new QuadListener(){
		public void addQuad(Quad q){
			Chunk.this.addQuad(q);
		}
	};
	private boolean needsRebuild;
	private final int chunkX, chunkY, chunkZ;
	private final World world;
	private int currentBlockCount;
	Chunk(int chunkX, int chunkY, int chunkZ, World world){
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		this.world = world;
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = Block.AIR;
	}
	Block getBlock(int x, int y, int z){
		int id = blocks[Chunk.getIndex(x, y, z)]+Block.ID_SHIFT;
		if(id==-1)return null;
		return Block.values()[id];
	}
	void setBlock(int x, int y, int z, Block block){
		int index = Chunk.getIndex(x, y, z);
		byte id = block==null?Block.AIR:(byte)(block.ordinal()-Block.ID_SHIFT);
		if(blocks[index]==id)return;
		needsRebuild = true;
		blocks[index] = id;
		if(block==null)currentBlockCount--;
		else currentBlockCount++;
	}
	void rebuild(){
		if(!needsRebuild)return;
		if(!containsBlocks())return;
		needsRebuild = false;
		clearAllBatches();
		int x, y, z, j, i, q;
		byte block;
		for(j = 0; j<6; j++){
			if(j==0
					||j==1){
				for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++){
					Chunk.PROPERTIES.reset();
					for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
						for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++){
							block = blocks[Chunk.getIndex(x, y, z)];
							if(block==Block.AIR)continue;
							if(!Chunk.PROPERTIES.contains(block))Chunk.PROPERTIES.place(block);
						}
					for(i = 0; i<Chunk.PROPERTIES.size(); i++){
						for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
							for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++)
								Chunk.QUAD_LAYER[y][z] = blocks[Chunk.getIndex(x, y, z)]==Chunk.PROPERTIES.get(i)
								&&isOpen(x, y, z, j);
						q = QuadOptimizer.optimize(Chunk.QUAD_STORAGE, Chunk.QUAD_STORAGE_2, Chunk.QUAD_LAYER, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, Chunk.HIGH_QUALITY_QUAD_MESHING);
						if(q==0)continue;
						Chunk.X_QUAD_COUNTER.setup(chunkX<<Chunk.CHUNK_BITS, chunkY<<Chunk.CHUNK_BITS, chunkZ<<Chunk.CHUNK_BITS, x, j, quadListener, Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(Chunk.X_QUAD_COUNTER, Chunk.QUAD_STORAGE, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}else if(j==2
					||j==3){
				for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++){
					Chunk.PROPERTIES.reset();
					for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
						for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++){
							block = blocks[Chunk.getIndex(x, y, z)];
							if(block==Block.AIR)continue;
							if(!Chunk.PROPERTIES.contains(block))Chunk.PROPERTIES.place(block);
						}
					for(i = 0; i<Chunk.PROPERTIES.size(); i++){
						for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
							for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++)
								Chunk.QUAD_LAYER[x][z] = blocks[Chunk.getIndex(x, y, z)]==Chunk.PROPERTIES.get(i)
								&&isOpen(x, y, z, j);
						q = QuadOptimizer.optimize(Chunk.QUAD_STORAGE, Chunk.QUAD_STORAGE_2, Chunk.QUAD_LAYER, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, Chunk.HIGH_QUALITY_QUAD_MESHING);
						if(q==0)continue;
						Chunk.Y_QUAD_COUNTER.setup(chunkX<<Chunk.CHUNK_BITS, chunkY<<Chunk.CHUNK_BITS, chunkZ<<Chunk.CHUNK_BITS, y, j, quadListener, Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(Chunk.Y_QUAD_COUNTER, Chunk.QUAD_STORAGE, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}else{
				for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++){
					Chunk.PROPERTIES.reset();
					for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
						for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++){
							block = blocks[Chunk.getIndex(x, y, z)];
							if(block==Block.AIR)continue;
							if(!Chunk.PROPERTIES.contains(block))Chunk.PROPERTIES.place(block);
						}
					for(i = 0; i<Chunk.PROPERTIES.size(); i++){
						for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
							for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
								Chunk.QUAD_LAYER[x][y] = blocks[Chunk.getIndex(x, y, z)]==Chunk.PROPERTIES.get(i)
								&&isOpen(x, y, z, j);
						q = QuadOptimizer.optimize(Chunk.QUAD_STORAGE, Chunk.QUAD_STORAGE_2, Chunk.QUAD_LAYER, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, Chunk.HIGH_QUALITY_QUAD_MESHING);
						if(q==0)continue;
						Chunk.Z_QUAD_COUNTER.setup(chunkX<<Chunk.CHUNK_BITS, chunkY<<Chunk.CHUNK_BITS, chunkZ<<Chunk.CHUNK_BITS, z, j, quadListener, Block.values()[PROPERTIES.get(i)+Block.ID_SHIFT]);
						QuadOptimizer.countQuads(Chunk.Z_QUAD_COUNTER, Chunk.QUAD_STORAGE, Chunk.BLOCKS_PER_CHUNK, Chunk.BLOCKS_PER_CHUNK, q);
					}
				}
			}
		}
		compile();
	}
	private boolean isOpen(int x, int y, int z, int j){
		if(j==0){
			if(x==Chunk.HIGH_BLOCK_COUNT)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x+1, (chunkY<<Chunk.CHUNK_BITS)+y, (chunkZ<<Chunk.CHUNK_BITS)+z, false)==null;
			return blocks[Chunk.getIndex(x+1, y, z)]==Block.AIR;
		}else if(j==1){
			if(x==0)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x-1, (chunkY<<Chunk.CHUNK_BITS)+y, (chunkZ<<Chunk.CHUNK_BITS)+z, false)==null;
			return blocks[Chunk.getIndex(x-1, y, z)]==Block.AIR;
		}else if(j==2){
			if(y==Chunk.HIGH_BLOCK_COUNT)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x, (chunkY<<Chunk.CHUNK_BITS)+y+1, (chunkZ<<Chunk.CHUNK_BITS)+z, false)==null;
			return blocks[Chunk.getIndex(x, y+1, z)]==Block.AIR;
		}else if(j==3){
			if(y==0)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x, (chunkY<<Chunk.CHUNK_BITS)+y-1, (chunkZ<<Chunk.CHUNK_BITS)+z, false)==null;
			return blocks[Chunk.getIndex(x, y-1, z)]==Block.AIR;
		}else if(j==4){
			if(z==Chunk.HIGH_BLOCK_COUNT)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x, (chunkY<<Chunk.CHUNK_BITS)+y, (chunkZ<<Chunk.CHUNK_BITS)+z+1, false)==null;
			return blocks[Chunk.getIndex(x, y, z+1)]==Block.AIR;
		}else{
			if(z==0)return world.getBlock((chunkX<<Chunk.CHUNK_BITS)+x, (chunkY<<Chunk.CHUNK_BITS)+y, (chunkZ<<Chunk.CHUNK_BITS)+z-1, false)==null;
			return blocks[Chunk.getIndex(x, y, z-1)]==Block.AIR;
		}
	}
	public boolean containsBlocks(){
		return currentBlockCount>0;
	}
}