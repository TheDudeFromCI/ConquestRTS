package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;
import wraithaven.conquest.client.BuildingCreator.Loop;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.BlockSideProperties;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkXQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkYQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkZQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.IndexManager;

public class Chunk{
	public static final int CHUNK_BITS = 1;
	public static final int BLOCKS_PER_CHUNK = (int)Math.pow(2, Chunk.CHUNK_BITS);
	public static final int HIGH_BLOCK_COUNT = Chunk.BLOCKS_PER_CHUNK-1;
	public static final int HIGH_SMALL_BLOCK = Chunk.BLOCKS_PER_CHUNK*8-1;
	public static final int CHUNK_Y_LENGTH = Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK;
	private static final int SMALL_BLOCKS_SQUARED = (int)Math.pow(BLOCKS_PER_CHUNK*8, 2);
	private static final ArrayList<BlockSideProperties> PROPS_TEMP_STORAGE = new ArrayList();
	private static final ChunkXQuadCounter QUAD_COUNTER_X = new ChunkXQuadCounter();
	private static final ChunkYQuadCounter QUAD_COUNTER_Y = new ChunkYQuadCounter();
	private static final ChunkZQuadCounter QUAD_COUNTER_Z = new ChunkZQuadCounter();
	public static final int SMALL_BLOCK_COUNT = Chunk.BLOCKS_PER_CHUNK*8;
	private static final boolean[][] TEMP_QUADS = new boolean[Chunk.SMALL_BLOCK_COUNT][Chunk.SMALL_BLOCK_COUNT];
	private static final int[][] TEMP_STORAGE = new int[Chunk.SMALL_BLOCK_COUNT][Chunk.SMALL_BLOCK_COUNT];
	private static final int[][] TEMP_STORAGE_2 = new int[Chunk.SMALL_BLOCK_COUNT][Chunk.SMALL_BLOCK_COUNT];
	private static int blockIndex(int x, int y, int z){
		return x+z*Chunk.BLOCKS_PER_CHUNK+y*Chunk.CHUNK_Y_LENGTH;
	}
	private static boolean makeWallForTransparency(Chunk chunk1, Chunk chunk2, int ox, int oy, int oz, int x, int y, int z){
		int index1 = Chunk.blockIndex(ox/8, oy/8, oz/8);
		int index2 = Chunk.blockIndex(x/8, y/8, z/8);
		if(index1==index2)return false;
		short loc1 = chunk1.blocks[index1];
		short loc2 = chunk2.blocks[index2];
		if(loc1==IndexManager.AIR_BLOCK)return false;
		if(loc2==IndexManager.AIR_BLOCK)return false;
		if(Loop.INSTANCE.getVoxelWorld().getIndexManager().getBlock(loc2).textures.transparent())return true;
		return false;
	}
	final ArrayList<QuadBatch> batches = new ArrayList(1);
	private final short[] blocks = new short[Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK*Chunk.BLOCKS_PER_CHUNK];
	public final int chunkX, chunkY, chunkZ;
	private int hidden = blocks.length;
	private boolean needsBatchUpdate;
	private boolean needsRebuild;
	public final int startX, startY, startZ, endX, endY, endZ;
	public final VoxelWorld world;
	Chunk(VoxelWorld world, int chunkX, int chunkY, int chunkZ){
		this.world = world;
		this.chunkX = chunkX;
		this.chunkY = chunkY;
		this.chunkZ = chunkZ;
		startX = chunkX*Chunk.BLOCKS_PER_CHUNK;
		startY = chunkY*Chunk.BLOCKS_PER_CHUNK;
		startZ = chunkZ*Chunk.BLOCKS_PER_CHUNK;
		endX = startX+Chunk.HIGH_BLOCK_COUNT;
		endY = startY+Chunk.HIGH_BLOCK_COUNT;
		endZ = startZ+Chunk.HIGH_BLOCK_COUNT;
		for(int i = 0; i<blocks.length; i++)
			blocks[i] = IndexManager.AIR_BLOCK;
	}
	void addHidden(){
		hidden++;
	}
	public void clearEmptyBatches(){
		for(int i = 0; i<batches.size();){
			if(batches.get(i).getSize()==0){
				batches.get(i).cleanUp();
				batches.remove(i);
			}else i++;
		}
	}
	private void countTextures(int i, int side){
		Chunk.PROPS_TEMP_STORAGE.clear();
		int x, y, z, index;
		BlockSideProperties props;
		Block block;
		if(side==0||side==1){
			x = i;
			for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++)
				for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++){
					index = Chunk.blockIndex(x, y, z);
					if(blocks[index]==IndexManager.AIR_BLOCK)continue;
					props = new BlockSideProperties();
					block = world.getIndexManager().getBlock(blocks[index]);
					props.texture = block.textures.getTexture(side);
					props.side = side;
					props.rotation = block.textures.getRotation(side);
					props.r = block.textures.colors[side*3];
					props.g = block.textures.colors[side*3+1];
					props.b = block.textures.colors[side*3+2];
					if(!Chunk.PROPS_TEMP_STORAGE.contains(props)) Chunk.PROPS_TEMP_STORAGE.add(props);
				}
		}
		if(side==2||side==3){
			y = i;
			for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
				for(z = 0; z<Chunk.BLOCKS_PER_CHUNK; z++){
					index = Chunk.blockIndex(x, y, z);
					if(blocks[index]==IndexManager.AIR_BLOCK)continue;
					props = new BlockSideProperties();
					block = world.getIndexManager().getBlock(blocks[index]);
					props.texture = block.textures.getTexture(side);
					props.side = side;
					props.rotation = block.textures.getRotation(side);
					props.r = block.textures.colors[side*3];
					props.g = block.textures.colors[side*3+1];
					props.b = block.textures.colors[side*3+2];
					if(!Chunk.PROPS_TEMP_STORAGE.contains(props)) Chunk.PROPS_TEMP_STORAGE.add(props);
				}
		}
		if(side==4||side==5){
			z = i;
			for(x = 0; x<Chunk.BLOCKS_PER_CHUNK; x++)
				for(y = 0; y<Chunk.BLOCKS_PER_CHUNK; y++){
					index = Chunk.blockIndex(x, y, z);
					if(blocks[index]==IndexManager.AIR_BLOCK)continue;
					props = new BlockSideProperties();
					block = world.getIndexManager().getBlock(blocks[index]);
					props.texture = block.textures.getTexture(side);
					props.side = side;
					props.rotation = block.textures.getRotation(side);
					props.r = block.textures.colors[side*3];
					props.g = block.textures.colors[side*3+1];
					props.b = block.textures.colors[side*3+2];
					if(!Chunk.PROPS_TEMP_STORAGE.contains(props)) Chunk.PROPS_TEMP_STORAGE.add(props);
				}
		}
	}
	public void dispose(){
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).cleanUp();
		batches.clear();
	}
	QuadBatch getBatch(Texture texture, int x, int y, int z){
		x += startX;
		y += startY;
		z += startZ;
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch = batches.get(i);
			if(batch.getTexture()==texture){
				if(texture.transparent
						&&((int)batch.centerX!=x
						||(int)batch.centerY!=y
						||(int)batch.centerZ!=z))continue;
				return batch;
			}
		}
		batch = new QuadBatch(texture, x+0.5f, y+0.5f, z+0.5f);
		batches.add(batch);
		return batch;
	}
	public int getBlock(int x, int y, int z){
		return blocks[Chunk.blockIndex(x&Chunk.HIGH_BLOCK_COUNT, y&Chunk.HIGH_BLOCK_COUNT, z&Chunk.HIGH_BLOCK_COUNT)];
	}
	private boolean getSmallBlock(int x, int y, int z, BlockSideProperties props){
		int blockX = x/8;
		int blockY = y/8;
		int blockZ = z/8;
		int index = Chunk.blockIndex(blockX, blockY, blockZ);
		if(blocks[index]==IndexManager.AIR_BLOCK)return false;
		Block block = world.getIndexManager().getBlock(blocks[index]);
		if(props==null
				||(block.textures.getTexture(props.side)==props.texture
				&&block.textures.getRotation(props.side)==props.rotation
				&&block.textures.colors[props.side*3]==props.r
				&&block.textures.colors[props.side*3+1]==props.g
				&&block.textures.colors[props.side*3+2]==props.b))return block.shape.getBlock(x&7, y&7, z&7, block.rotation);
		return false;
	}
	private boolean getSmallBlockNeighbor(Chunk chunk1, Chunk chunk2, int ox, int oy, int oz, int x, int y, int z){
		if(makeWallForTransparency(chunk1, chunk2, ox, oy, oz, x, y, z))return false;
		int index = Chunk.blockIndex(x/8, y/8, z/8);
		if(blocks[index]==IndexManager.AIR_BLOCK)return false;
		Block block = world.getIndexManager().getBlock(blocks[index]);
		return block.shape.getBlock(x&7, y&7, z&7, block.rotation);
	}
	private boolean hasSmallNeighbor(int x, int y, int z, int side){
		if(side==0)return x==Chunk.HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX+1, chunkY, chunkZ, 0, y, z, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x+1, y, z);
		if(side==1)return x==0?hasSmallNeighbor(chunkX-1, chunkY, chunkZ, Chunk.HIGH_SMALL_BLOCK, y, z, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x-1, y, z);
		if(side==2)return y==Chunk.HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX, chunkY+1, chunkZ, x, 0, z, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x, y+1, z);
		if(side==3)return y==0?hasSmallNeighbor(chunkX, chunkY-1, chunkZ, x, Chunk.HIGH_SMALL_BLOCK, z, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x, y-1, z);
		if(side==4)return z==Chunk.HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX, chunkY, chunkZ+1, x, y, 0, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x, y, z+1);
		if(side==5)return z==0?hasSmallNeighbor(chunkX, chunkY, chunkZ-1, x, y, Chunk.HIGH_SMALL_BLOCK, x, y, z):getSmallBlockNeighbor(this, this, x, y, z, x, y, z-1);
		return false;
	}
	private boolean hasSmallNeighbor(int chunkX, int chunkY, int chunkZ, int x, int y, int z, int ox, int oy, int oz){
		if(world.bounds!=null&&!world.bounds.isWithinBounds(chunkX, chunkY, chunkZ))return true;
		Chunk c = world.getChunk(chunkX, chunkY, chunkZ, false);
		if(c==null)return false;
		return c.getSmallBlockNeighbor(this, c, ox, oy, oz, x, y, z);
	}
	public boolean isHidden(){
		return hidden==blocks.length;
	}
	public boolean needsRebatch(){
		return needsBatchUpdate;
	}
	private void optimizeSideX(int x, int side, QuadBatch batch, BlockSideProperties props, int pos1, int pos2, int size){
		int y, z, q;
		for(y = 0; y<Chunk.SMALL_BLOCK_COUNT; y++)
			for(z = 0; z<Chunk.SMALL_BLOCK_COUNT; z++)
				Chunk.TEMP_QUADS[y][z] = false;
		int blocks = 0;
		for(y = pos1; y<pos1+size; y++)
			for(z = pos2; z<pos2+size; z++){
				Chunk.TEMP_QUADS[y][z] = getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
				if(Chunk.TEMP_QUADS[y][z])blocks++;
			}
		if(blocks==Chunk.SMALL_BLOCKS_SQUARED)return;
		if((q = QuadOptimizer.optimize(Chunk.TEMP_STORAGE, Chunk.TEMP_STORAGE_2, Chunk.TEMP_QUADS, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT))>0){
			Chunk.QUAD_COUNTER_X.setup(startX, startY, startZ, x, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(Chunk.QUAD_COUNTER_X, Chunk.TEMP_STORAGE, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT, q);
		}
	}
	private void optimizeSideY(int y, int side, QuadBatch batch, BlockSideProperties props, int pos1, int pos2, int size){
		int x, z, q;
		for(x = 0; x<Chunk.SMALL_BLOCK_COUNT; x++)
			for(z = 0; z<Chunk.SMALL_BLOCK_COUNT; z++)
				Chunk.TEMP_QUADS[x][z] = false;
		int blocks = 0;
		for(x = pos1; x<pos1+size; x++)
			for(z = pos2; z<pos2+size; z++){
				Chunk.TEMP_QUADS[x][z] = getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
				if(Chunk.TEMP_QUADS[x][z])blocks++;
			}
		if(blocks==Chunk.SMALL_BLOCKS_SQUARED)return;
		if((q = QuadOptimizer.optimize(Chunk.TEMP_STORAGE, Chunk.TEMP_STORAGE_2, Chunk.TEMP_QUADS, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT))>0){
			Chunk.QUAD_COUNTER_Y.setup(startX, startY, startZ, y, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(Chunk.QUAD_COUNTER_Y, Chunk.TEMP_STORAGE, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT, q);
		}
	}
	private void optimizeSideZ(int z, int side, QuadBatch batch, BlockSideProperties props, int pos1, int pos2, int size){
		int y, x, q;
		for(x = 0; x<Chunk.SMALL_BLOCK_COUNT; x++)
			for(y = 0; y<Chunk.SMALL_BLOCK_COUNT; y++)
				Chunk.TEMP_QUADS[x][y] = false;
		int blocks = 0;
		for(x = pos1; x<pos1+size; x++)
			for(y = pos2; y<pos2+size; y++){
				Chunk.TEMP_QUADS[x][y] = getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
				if(Chunk.TEMP_QUADS[x][y])blocks++;
			}
		if(blocks==Chunk.SMALL_BLOCKS_SQUARED)return;
		if((q = QuadOptimizer.optimize(Chunk.TEMP_STORAGE, Chunk.TEMP_STORAGE_2, Chunk.TEMP_QUADS, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT))>0){
			Chunk.QUAD_COUNTER_Z.setup(startX, startY, startZ, z, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(Chunk.QUAD_COUNTER_Z, Chunk.TEMP_STORAGE, Chunk.SMALL_BLOCK_COUNT, Chunk.SMALL_BLOCK_COUNT, q);
		}
	}
	public void rebuild(){
		if(!needsRebuild) return;
		needsRebuild = false;
		needsBatchUpdate = true;
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).clear();
		int i, j, k, l, p1, p2, sub;
		BlockSideProperties props;
		QuadBatch batch;
		for(j = 0; j<6; j++)
			for(i = 0; i<Chunk.BLOCKS_PER_CHUNK; i++){
				sub = i*8;
				countTextures(i, j);
				for(l = 0; l<Chunk.PROPS_TEMP_STORAGE.size(); l++){
					props = Chunk.PROPS_TEMP_STORAGE.get(l);
					if(props.texture.transparent){
						for(p1 = 0; p1<Chunk.BLOCKS_PER_CHUNK; p1++)
							for(p2 = 0; p2<Chunk.BLOCKS_PER_CHUNK; p2++)
								if(j==0||j==1){
									batch = getBatch(props.texture, i, p1, p2);
									for(k = 0; k<8; k++)
										optimizeSideX(sub+k, j, batch, props, p1*8, p2*8, 8);
								}else if(j==2||j==3){
									batch = getBatch(props.texture, p1, i, p2);
									for(k = 0; k<8; k++)
										optimizeSideY(sub+k, j, batch, props, p1*8, p2*8, 8);
								}else{
									batch = getBatch(props.texture, p1, p2, i);
									for(k = 0; k<8; k++)
										optimizeSideZ(sub+k, j, batch, props, p1*8, p2*8, 8);
								}
					}else{
						batch = getBatch(props.texture, 0, 0, 0);
						for(k = 0; k<8; k++)
							if(j==0||j==1)optimizeSideX(sub+k, j, batch, props, 0, 0, Chunk.SMALL_BLOCK_COUNT);
							else if(j==2||j==3)optimizeSideY(sub+k, j, batch, props, 0, 0, Chunk.SMALL_BLOCK_COUNT);
							else optimizeSideZ(sub+k, j, batch, props, 0, 0, Chunk.SMALL_BLOCK_COUNT);
					}
				}
			}
		Chunk.PROPS_TEMP_STORAGE.clear();
	}
	void removeHidden(){
		hidden--;
	}
	public boolean setBlock(int x, int y, int z, short blockIndex){
		x &= Chunk.HIGH_BLOCK_COUNT;
		y &= Chunk.HIGH_BLOCK_COUNT;
		z &= Chunk.HIGH_BLOCK_COUNT;
		int index = Chunk.blockIndex(x, y, z);
		if(blocks[index]==blockIndex)return false;
		blocks[index] = blockIndex;
		removeHidden();
		needsBatchUpdate = true;
		needsRebuild = true;
		updateNearbyChunks(x, y, z);
		return true;
	}
	void updateBatches(){
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).recompileBuffer();
		needsBatchUpdate = false;
	}
	private void updateNearbyChunks(int x, int y, int z){
		Chunk c;
		if(x==0){
			c = world.getChunk(chunkX-1, chunkY, chunkZ, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
		if(x==Chunk.HIGH_BLOCK_COUNT){
			c = world.getChunk(chunkX+1, chunkY, chunkZ, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
		if(y==0){
			c = world.getChunk(chunkX, chunkY-1, chunkZ, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
		if(y==Chunk.HIGH_BLOCK_COUNT){
			c = world.getChunk(chunkX, chunkY+1, chunkZ, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
		if(z==0){
			c = world.getChunk(chunkX, chunkY, chunkZ-1, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
		if(z==Chunk.HIGH_BLOCK_COUNT){
			c = world.getChunk(chunkX, chunkY, chunkZ+1, false);
			if(c!=null){
				c.needsRebuild = true;
				c.needsBatchUpdate = true;
			}
		}
	}
}