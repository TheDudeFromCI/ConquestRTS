package wraithaven.conquest.client.GameWorld.Voxel;

import java.util.ArrayList;

public class Chunk{
	private boolean open;
	private boolean needsBatchUpdate;
	private final Block[] blocks = new Block[BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK];
	private int hidden = blocks.length;
	public final int chunkX, chunkY, chunkZ;
	public final int startX, startY, startZ, endX, endY, endZ;
	final ArrayList<QuadBatch> batches = new ArrayList(1);
	public final VoxelWorld world;
	public static final int BLOCKS_PER_CHUNK = 16;
	public static final int CHUNK_BITS = 4;
	private static final int HIGH_BLOCK_COUNT = BLOCKS_PER_CHUNK-1;
	private static final int X_OFFSET = 1;
	private static final int Y_OFFSET = BLOCKS_PER_CHUNK;
	private static final int Z_OFFSET = BLOCKS_PER_CHUNK*BLOCKS_PER_CHUNK;
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
	}
	public Block createBlock(int x, int y, int z, BlockShape shape, CubeTextures textures, BlockRotation rotation){
		int index = getIndex(x, y, z);
		blocks[index]=new CustomBlock(this, x, y, z, shape, textures, rotation);
		removeHidden();
		((CustomBlock)blocks[index]).build();
		return blocks[index];
	}
	public Block createBlock(int x, int y, int z){
		int index = getIndex(x, y, z);
		blocks[index]=new Block(this, x, y, z);
		removeHidden();
		return blocks[index];
	}
	public void optimizeSide(int side){
		if(side==0){
			int y, z;
			for(y=0; y<BLOCKS_PER_CHUNK; y++)for(z=0; z<BLOCKS_PER_CHUNK; z++)optimizeBlock(blocks[getIndex(HIGH_BLOCK_COUNT, y, z)], 0, true);
		}
		if(side==1){
			int y, z;
			for(y=0; y<BLOCKS_PER_CHUNK; y++)for(z=0; z<BLOCKS_PER_CHUNK; z++)optimizeBlock(blocks[getIndex(0, y, z)], 1, true);
		}
		if(side==2){
			int x, z;
			for(x=0; x<BLOCKS_PER_CHUNK; x++)for(z=0; z<BLOCKS_PER_CHUNK; z++)optimizeBlock(blocks[getIndex(x, HIGH_BLOCK_COUNT, z)], 2, true);
		}
		if(side==3){
			int x, z;
			for(x=0; x<BLOCKS_PER_CHUNK; x++)for(z=0; z<BLOCKS_PER_CHUNK; z++)optimizeBlock(blocks[getIndex(x, 0, z)], 3, true);
		}
		if(side==4){
			int x, y;
			for(x=0; x<BLOCKS_PER_CHUNK; x++)for(y=0; y<BLOCKS_PER_CHUNK; y++)optimizeBlock(blocks[getIndex(x, y, HIGH_BLOCK_COUNT)], 4, true);
		}
		if(side==5){
			int x, y;
			for(x=0; x<BLOCKS_PER_CHUNK; x++)for(y=0; y<BLOCKS_PER_CHUNK; y++)optimizeBlock(blocks[getIndex(x, y, 0)], 5, true);
		}
	}
	public void optimizeBlock(Block block){
		if(block==null)return;
		optimizeBlock(block, 0, true);
		optimizeBlock(block, 1, true);
		optimizeBlock(block, 2, true);
		optimizeBlock(block, 3, true);
		optimizeBlock(block, 4, true);
		optimizeBlock(block, 5, true);
	}
	private void optimizeBlock(Block block, int side, boolean updateShadows){
		if(block==null)return;
		if(block instanceof CustomBlock){
			((CustomBlock)block).optimizeSide(side);
			if(updateShadows){
				if(block.quads[side]!=null){
					block.quads[side].centerPoint=block.setupShadows(block.quads[side].data, side, block.x, block.y, block.z);
					((CustomBlock)block).calculateColors(block.quads[side].data, side);
					block.chunk.setNeedsRebatch();
				}
			}
			return;
		}
		open=block.chunk.isNeighborOpen(block, side);
		if(open!=block.isSideShown(side)){
			block.chunk.setNeedsRebatch();
			if(open){
				block.showSide(side, open);
				block.chunk.getBatch(Block.getTexture(side), false, 0, 0, 0).addQuad(block.getQuad(side));
			}else{
				block.chunk.getBatch(Block.getTexture(side), false, 0, 0, 0).removeQuad(block.getQuad(side));
				block.showSide(side, open);
			}
		}
		if(updateShadows){
			if(block.quads[side]!=null){
				block.quads[side].centerPoint=block.setupShadows(block.quads[side].data, side, block.x, block.y, block.z);
				block.chunk.setNeedsRebatch();
			}
		}
	}
	public void setNeedsRebatch(){
		needsBatchUpdate=true;
		world.setNeedsRebatch();
	}
	private void optimizeAroundBlock(int x, int y, int z){
		int startX = x-1;
		int startY = y-1;
		int startZ = z-1;
		int endX = x+1;
		int endY = y+1;
		int endZ = z+1;
		int sx, sy, sz;
		for(sx=startX; sx<=endX; sx++){
			for(sy=startY; sy<=endY; sy++){
				for(sz=startZ; sz<=endZ; sz++){
					if(sx==x&&sy==y&&sz==z)continue;
					optimizeBlock(getQuickBlock(sx, sy, sz));
				}
			}
		}
	}
	private void removeBlock(int x, int y, int z){
		int index = getIndex(x, y, z);
		if(blocks[index]==null)return;
		if(blocks[index] instanceof CustomBlock)((CustomBlock)blocks[index]).destroy();
		else removeBlockQuads(blocks[index]);
		blocks[index]=null;
	}
	public Block setBlock(int x, int y, int z, BlockShape shape, CubeTextures textures, BlockRotation rotation){
		setNeedsRebatch();
		removeBlock(x, y, z);
		if(shape!=null){
			Block block = createBlock(x, y, z, shape, textures, rotation);
			optimizeBlock(block);
			optimizeAroundBlock(x, y, z);
			return block;
		}
		optimizeAroundBlock(x, y, z);
		return null;
	}
	private boolean isNeighborOpen(Block block, int side){
		if(side==0)return (world.bounds==null||block.x<world.bounds.endX)&&neighborFull(block.x+1, block.y, block.z, side);
		if(side==1)return (world.bounds==null||block.x>world.bounds.startX)&&neighborFull(block.x-1, block.y, block.z, side);
		if(side==2)return (world.bounds==null||block.y<world.bounds.endY)&&neighborFull(block.x, block.y+1, block.z, side);
		if(side==3)return (world.bounds==null||block.y>world.bounds.startY)&&neighborFull(block.x, block.y-1, block.z, side);
		if(side==4)return (world.bounds==null||block.z<world.bounds.endZ)&&neighborFull(block.x, block.y, block.z+1, side);
		if(side==5)return (world.bounds==null||block.z>world.bounds.startZ)&&neighborFull(block.x, block.y, block.z-1, side);
		return true;
	}
	private boolean neighborFull(int x, int y, int z, int side){
		Block block = getQuickBlock(x, y, z);
		if(block==null)return true;
		if(block instanceof CustomBlock)return !((CustomBlock)block).shape.fullSide(CustomBlock.oppositeSide(side), ((CustomBlock)block).rotation);
		return false;
	}
	QuadBatch getBatch(Texture texture, boolean small, int x, int y, int z){
		x=(x&HIGH_BLOCK_COUNT)/4;
		y=(y&HIGH_BLOCK_COUNT)/4;
		z=(z&HIGH_BLOCK_COUNT)/4;
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch=batches.get(i);
			if(batch.getTexture()==texture&&batch.small==small){
				if(small){
					if(batch.x==x&&batch.y==y&&batch.z==z)return batch;
					continue;
				}
				return batch;
			}
		}
		batch=new QuadBatch(texture, small, x, y, z);
		batches.add(batch);
		return batch;
	}
	private Block getQuickBlock(int x, int y, int z){
		if(x<startX||y<startY||z<startZ||x>endX||y>endY||z>endZ)return world.getBlock(x, y, z, false);
		return blocks[getIndex(x, y, z)];
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
			}
			else i++;
		}
	}
	public Block getBlock(int x, int y, int z){ return blocks[getIndex(x, y, z)]; }
	public void dispose(){ for(int i = 0; i<batches.size(); i++)batches.get(i).cleanUp(); }
	void addHidden(){ hidden++; }
	void removeHidden(){ hidden--; }
	public boolean isHidden(){ return hidden==blocks.length; }
	private void removeBlockQuads(Block block){ for(int i = 0; i<6; i++)getBatch(Block.getTexture(i), false, 0, 0, 0).removeQuad(block.getQuad(i)); }
	public void optimize(){ for(int i = 0; i<blocks.length; i++)optimizeBlock(blocks[i]); }
	public boolean needsRebatch(){ return needsBatchUpdate; }
	private static int getIndex(int x, int y, int z){ return (x&HIGH_BLOCK_COUNT)*X_OFFSET+(y&HIGH_BLOCK_COUNT)*Y_OFFSET+(z&HIGH_BLOCK_COUNT)*Z_OFFSET; }
}