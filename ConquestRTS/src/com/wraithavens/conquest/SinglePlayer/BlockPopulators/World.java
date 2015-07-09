package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.WorldGenerator;
import com.wraithavens.conquest.Utility.LoadingScreen;
import com.wraithavens.conquest.Utility.LoadingScreenTask;

public class World{
	private final boolean created;
	private boolean needsRebuffer;
	private VoxelWorld world;
	private WorldGenerator worldGen;
	public World(){
		created = false;
		world = new VoxelWorld(8);
	}
	public void initalize(SinglePlayerGame game){
		Block.loadTextures();
		if(created){
		}else{
			worldGen = new WorldGenerator();
			LoadingScreen loadingScreen = new LoadingScreen(new LoadingScreenTask(){
				int size = 50;
				int x = -size, y, z = -size, h;
				int columns = 0;
				int lastPercent = 0;
				public boolean runStep(){
					for(int i = 0; i<64; i++){
						placeColumn();
						if(nextBlock()){
							System.out.println("Loading world... 100%");
							rebuildAllChunks();
							return true;
						}
					}
					columns += 64;
					double realPercent = (columns/(double)((size+size+1)*(size+size+1))*100);
					int percent = (int)realPercent;
					if(lastPercent!=percent){
						lastPercent = percent;
						System.out.println("Loading world... "+lastPercent+"%");
					}
					return false;
				}
				private void placeColumn(){
					h = worldGen.getHeightAt(x, z);
					for(y = 0; y<h; y++)
						setBlock(x, y, z, Block.GRASS, false);
				}
				private boolean nextBlock(){
					z++;
					if(z>size){
						z = -size;
						x++;
						if(x>size)return true;
					}
					return false;
				}
			}, game);
			WraithavensConquest.INSTANCE.setDriver(loadingScreen);
		}
	}
	private void setBlock(int x, int y, int z, Block block, boolean rebuild){
		int chunkX = x>>Chunk.CHUNK_BITS;
		int chunkY = y>>Chunk.CHUNK_BITS;
		int chunkZ = z>>Chunk.CHUNK_BITS;
		EmptyChunk c = world.getSubVoxelAt(chunkX, chunkY, chunkZ, true).getState();
		Chunk chunk;
		if(c==Voxel.DEFAULT){
			chunk = new Chunk(chunkX, chunkY, chunkZ, this);
			world.fillArea(chunkX, chunkY, chunkZ, 1, 1, 1, chunk);
		}else chunk = (Chunk)c;
		chunk.setBlock(x, y, z, block);
		if(rebuild){
			chunk.rebuild();
			needsRebuffer = true;
		}
	}
	private void rebuildAllChunks(){
		for(int i = 0; i<world.voxels.size(); i++)
			rebuild(world.voxels.get(i));
		needsRebuffer = true;
	}
	private void rebuild(Voxel vox){
		if(vox.isSolid()){
			if(vox.getState()!=Voxel.DEFAULT)((Chunk)vox.getState()).rebuild();
			return;
		}
		for(int i = 0; i<8; i++)
			rebuild(vox.getVoxel(i));
	}
	public void dispose(){
		System.out.println("World disposed.");
		for(int i = 0; i<world.voxels.size(); i++)
			dispose(world.voxels.get(i));
		world = null;
	}
	private void dispose(Voxel vox){
		if(vox.isSolid()){
			if(vox.getState()!=Voxel.DEFAULT)((Chunk)vox.getState()).clearAllBatches();
			return;
		}
		for(int i = 0; i<8; i++)
			dispose(vox.getVoxel(i));
	}
	boolean needsRebuffer(){
		return needsRebuffer;
	}
	void setRebuffered(){
		needsRebuffer = false;
	}
	VoxelWorld getWorldStorage(){
		return world;
	}
	Block getBlock(int x, int y, int z, boolean load){
		int chunkX = x>>Chunk.CHUNK_BITS;
		int chunkY = y>>Chunk.CHUNK_BITS;
		int chunkZ = z>>Chunk.CHUNK_BITS;
		EmptyChunk c = world.getSubVoxelAt(chunkX, chunkY, chunkZ, true).getState();
		Chunk chunk;
		if(c==Voxel.DEFAULT){
			if(!load)return null;
			chunk = new Chunk(chunkX, chunkY, chunkZ, this);
			world.fillArea(chunkX, chunkY, chunkZ, 1, 1, 1, chunk);
		}else chunk = (Chunk)c;
		return chunk.getBlock(x, y, z);
	}
	public void setNeedsRebuffer(){
		needsRebuffer = true;
	}
	public int getHeightAt(float x, float z){
		return worldGen.getHeightAt(x, z);
	}
}