package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.BlockSideProperties;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkYQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkZQuadCounter;
import wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing.ChunkXQuadCounter;

public class Chunk{
	private boolean needsBatchUpdate;
	private boolean needsRebuild;
	private final short[] blocks = new short[BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK];
	private int hidden = blocks.length;
	public final int chunkX, chunkY, chunkZ;
	public final int startX, startY, startZ, endX, endY, endZ;
	final ArrayList<QuadBatch> batches = new ArrayList(1);
	public final VoxelWorld world;
	public static final int CHUNK_BITS = 1;
	public static final int BLOCKS_PER_CHUNK = (int)Math.pow(2, CHUNK_BITS);
	public static final int HIGH_BLOCK_COUNT = BLOCKS_PER_CHUNK-1;
	public static final int SMALL_BLOCK_COUNT = BLOCKS_PER_CHUNK*8;
	public static final int HIGH_SMALL_BLOCK = BLOCKS_PER_CHUNK*8-1;
	private static final int[][] TEMP_STORAGE = new int[SMALL_BLOCK_COUNT][SMALL_BLOCK_COUNT];
	private static final int[][] TEMP_STORAGE_2 = new int[SMALL_BLOCK_COUNT][SMALL_BLOCK_COUNT];
	private static final boolean[][] TEMP_QUADS = new boolean[SMALL_BLOCK_COUNT][SMALL_BLOCK_COUNT];
	private static final ChunkXQuadCounter QUAD_COUNTER_X = new ChunkXQuadCounter();
	private static final ChunkYQuadCounter QUAD_COUNTER_Y = new ChunkYQuadCounter();
	private static final ChunkZQuadCounter QUAD_COUNTER_Z = new ChunkZQuadCounter();
	private static final ArrayList<BlockSideProperties> PROPS_TEMP_STORAGE = new ArrayList();
	Chunk(VoxelWorld world, int chunkX, int chunkY, int chunkZ){
		this.world=world;
		this.chunkX=chunkX;
		this.chunkY=chunkY;
		this.chunkZ=chunkZ;
		startX=chunkX*BLOCKS_PER_CHUNK;
		startY=chunkY*BLOCKS_PER_CHUNK;
		startZ=chunkZ*BLOCKS_PER_CHUNK;
		endX=startX+HIGH_BLOCK_COUNT;
		endY=startY+HIGH_BLOCK_COUNT;
		endZ=startZ+HIGH_BLOCK_COUNT;
		for(int i = 0; i<blocks.length; i++)blocks[i]=-1;
	}
	public boolean setBlock(int x, int y, int z, short blockIndex){
		x&=HIGH_BLOCK_COUNT;
		y&=HIGH_BLOCK_COUNT;
		z&=HIGH_BLOCK_COUNT;
		int index = blockIndex(x, y, z);
		if(blocks[index]==blockIndex)return false;
		blocks[index]=blockIndex;
		removeHidden();
		needsBatchUpdate=true;
		needsRebuild=true;
		updateNearbyChunks(x, y, z);
		return true;
	}
	private void updateNearbyChunks(int x, int y, int z){
		Chunk c;
		if(x==0){
			c=world.getChunk(chunkX-1, chunkY, chunkZ, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
		if(x==HIGH_BLOCK_COUNT){
			c=world.getChunk(chunkX+1, chunkY, chunkZ, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
		if(y==0){
			c=world.getChunk(chunkX, chunkY-1, chunkZ, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
		if(y==HIGH_BLOCK_COUNT){
			c=world.getChunk(chunkX, chunkY+1, chunkZ, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
		if(z==0){
			c=world.getChunk(chunkX, chunkY, chunkZ-1, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
		if(z==HIGH_BLOCK_COUNT){
			c=world.getChunk(chunkX, chunkY, chunkZ+1, false);
			if(c!=null){
				c.needsRebuild=true;
				c.needsBatchUpdate=true;
			}
		}
	}
	QuadBatch getBatch(Texture texture){
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch=batches.get(i);
			if(batch.getTexture()==texture)return batch;
		}
		batch=new QuadBatch(texture, startX+BLOCKS_PER_CHUNK/2f, startY+BLOCKS_PER_CHUNK/2f, startZ+BLOCKS_PER_CHUNK/2f);
		batches.add(batch);
		return batch;
	}
	void updateBatches(){
		for(int i = 0; i<batches.size(); i++)batches.get(i).recompileBuffer();
		needsBatchUpdate=false;
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
		PROPS_TEMP_STORAGE.clear();
		int x, y, z, index;
		BlockSideProperties props;
		Block block;
		if(side==0||side==1){
			x=i;
			for(y=0; y<BLOCKS_PER_CHUNK; y++){
				for(z=0; z<BLOCKS_PER_CHUNK; z++){
					index=blockIndex(x, y, z);
					if(blocks[index]==-1)continue;
					props=new BlockSideProperties();
					block=world.getIndexManager().getBlock(blocks[index]);
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
		if(side==2||side==3){
			y=i;
			for(x=0; x<BLOCKS_PER_CHUNK; x++){
				for(z=0; z<BLOCKS_PER_CHUNK; z++){
					index=blockIndex(x, y, z);
					if(blocks[index]==-1)continue;
					props=new BlockSideProperties();
					block=world.getIndexManager().getBlock(blocks[index]);
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
		if(side==4||side==5){
			z=i;
			for(x=0; x<BLOCKS_PER_CHUNK; x++){
				for(y=0; y<BLOCKS_PER_CHUNK; y++){
					index=blockIndex(x, y, z);
					if(blocks[index]==-1)continue;
					props=new BlockSideProperties();
					block=world.getIndexManager().getBlock(blocks[index]);
					props.texture=block.textures.getTexture(side);
					props.side=side;
					props.rotation=block.textures.getRotation(side);
					props.r=block.textures.colors[side*3];
					props.g=block.textures.colors[side*3+1];
					props.b=block.textures.colors[side*3+2];
					if(!PROPS_TEMP_STORAGE.contains(props))PROPS_TEMP_STORAGE.add(props);
				}
			}
		}
	}
	public void rebuild(){
		if(!needsRebuild)return;
		needsRebuild=false;
		needsBatchUpdate=true;
		for(int i = 0; i<batches.size(); i++)batches.get(i).clear();
		int i, j, k, l;
		QuadBatch batch;
		for(j=0; j<6; j++){
			for(i=0; i<BLOCKS_PER_CHUNK; i++){
				countTextures(i, j);
				for(l=0; l<PROPS_TEMP_STORAGE.size(); l++){
					batch=getBatch(PROPS_TEMP_STORAGE.get(l).texture);
					for(k=0; k<8; k++){
						if(j==0||j==1)optimizeSideX(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
						if(j==2||j==3)optimizeSideY(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
						if(j==4||j==5)optimizeSideZ(i*8+k, j, batch, PROPS_TEMP_STORAGE.get(l));
					}
				}
			}
		}
		PROPS_TEMP_STORAGE.clear();
	}
	private boolean getSmallBlock(int x, int y, int z, BlockSideProperties props){
		int blockX = x/8;
		int blockY = y/8;
		int blockZ = z/8;
		int index = blockIndex(blockX, blockY, blockZ);
		if(blocks[index]==-1)return false;
		Block block = world.getIndexManager().getBlock(blocks[index]);
		if(props==null
				||(block.textures.getTexture(props.side)==props.texture
				&&block.textures.getRotation(props.side)==props.rotation
				&&block.textures.colors[props.side*3]==props.r
				&&block.textures.colors[props.side*3+1]==props.g
				&&block.textures.colors[props.side*3+2]==props.b))return block.shape.getBlock(x&7, y&7, z&7, block.rotation);
		return false;
	}
	private boolean getSmallBlockNeighbor(int ox, int oy, int oz, int x, int y, int z){
		int blockX = x/8;
		int blockY = y/8;
		int blockZ = z/8;
		int index = blockIndex(blockX, blockY, blockZ);
		if(blocks[index]==-1)return false;
		Block block = world.getIndexManager().getBlock(blocks[index]);
		if((ox/8!=x/8)||(oy/8!=y/8)||(oz/8!=z/8)&&block.textures.transparent())return false;
		return block.shape.getBlock(x&7, y&7, z&7, block.rotation);
	}
	private boolean hasSmallNeighbor(int x, int y, int z, int side){
		if(side==0)return x==HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX+1, chunkY, chunkZ, 0, y, z, x, y, z):getSmallBlockNeighbor(x, y, z, x+1, y, z);
		if(side==1)return x==0?hasSmallNeighbor(chunkX-1, chunkY, chunkZ, HIGH_SMALL_BLOCK, y, z, x, y, z):getSmallBlockNeighbor(x, y, z, x-1, y, z);
		if(side==2)return y==HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX, chunkY+1, chunkZ, x, 0, z, x, y, z):getSmallBlockNeighbor(x, y, z, x, y+1, z);
		if(side==3)return y==0?hasSmallNeighbor(chunkX, chunkY-1, chunkZ, x, HIGH_SMALL_BLOCK, z, x, y, z):getSmallBlockNeighbor(x, y, z, x, y-1, z);
		if(side==4)return z==HIGH_SMALL_BLOCK?hasSmallNeighbor(chunkX, chunkY, chunkZ+1, x, y, 0, x, y, z):getSmallBlockNeighbor(x, y, z, x, y, z+1);
		if(side==5)return z==0?hasSmallNeighbor(chunkX, chunkY, chunkZ-1, x, y, HIGH_SMALL_BLOCK, x, y, z):getSmallBlockNeighbor(x, y, z, x, y, z-1);
		return false;
	}
	private boolean hasSmallNeighbor(int chunkX, int chunkY, int chunkZ, int x, int y, int z, int ox, int oy, int oz){
		if(world.bounds!=null&&!world.bounds.isWithinBounds(chunkX, chunkY, chunkZ))return true;
		Chunk c = world.getChunk(chunkX, chunkY, chunkZ, false);
		if(c==null)return false;
		return c.getSmallBlockNeighbor(ox, oy, oz, x, y, z);
	}
	private void optimizeSideX(int x, int side, QuadBatch batch, BlockSideProperties props){
		int y, z, q;
		for(y=0; y<SMALL_BLOCK_COUNT; y++)for(z=0; z<SMALL_BLOCK_COUNT; z++)TEMP_QUADS[y][z]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT))>0){
			QUAD_COUNTER_X.setup(startX, startY, startZ, x, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_X, TEMP_STORAGE, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT, q);
		}
	}
	private void optimizeSideY(int y, int side, QuadBatch batch, BlockSideProperties props){
		int x, z, q;
		for(x=0; x<SMALL_BLOCK_COUNT; x++)for(z=0; z<SMALL_BLOCK_COUNT; z++)TEMP_QUADS[x][z]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT))>0){
			QUAD_COUNTER_Y.setup(startX, startY, startZ, y, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_Y, TEMP_STORAGE, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT, q);
		}
	}
	private void optimizeSideZ(int z, int side, QuadBatch batch, BlockSideProperties props){
		int y, x, q;
		for(x=0; x<SMALL_BLOCK_COUNT; x++)for(y=0; y<SMALL_BLOCK_COUNT; y++)TEMP_QUADS[x][y]=getSmallBlock(x, y, z, props)&&!hasSmallNeighbor(x, y, z, side);
		if((q=QuadOptimizer.optimize(TEMP_STORAGE, TEMP_STORAGE_2, TEMP_QUADS, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT))>0){
			QUAD_COUNTER_Z.setup(startX, startY, startZ, z, side, batch, props.rotation, props.r, props.g, props.b);
			QuadOptimizer.countQuads(QUAD_COUNTER_Z, TEMP_STORAGE, SMALL_BLOCK_COUNT, SMALL_BLOCK_COUNT, q);
		}
	}
	public void dispose(){
		for(int i = 0; i<batches.size(); i++)batches.get(i).cleanUp();
		batches.clear();
	}
	void addHidden(){ hidden++; }
	void removeHidden(){ hidden--; }
	public boolean isHidden(){ return hidden==blocks.length; }
	public boolean needsRebatch(){ return needsBatchUpdate; }
	public int getBlock(int x, int y, int z){ return blocks[blockIndex(x&HIGH_BLOCK_COUNT, y&HIGH_BLOCK_COUNT, z&HIGH_BLOCK_COUNT)]; }
	private static int blockIndex(int x, int y, int z){ return x+z*BLOCKS_PER_CHUNK+y*BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK; }
}