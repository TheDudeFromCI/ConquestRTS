package com.wraithavens.conquest.SinglePlayer.BlockPopulators;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import com.wraithavens.conquest.Launcher.WraithavensConquest;
import com.wraithavens.conquest.SinglePlayer.SinglePlayerGame;
import com.wraithavens.conquest.SinglePlayer.RenderHelpers.Texture;
import com.wraithavens.conquest.Utility.LoadingScreen;
import com.wraithavens.conquest.Utility.LoadingScreenTask;

public class World{
	private final boolean created;
	private boolean needsRebuffer;
	private VoxelWorld world;
	private WorldGenerator worldGen;
	public Texture shadowTexture;
	private ArrayList<int[]> sphereLocations = new ArrayList();
	public World(){
		created = false;
		world = new VoxelWorld(8);
	}
	public void dispose(){
		System.out.println("World disposed.");
		for(int i = 0; i<world.voxels.size(); i++)
			dispose(world.voxels.get(i));
		world = null;
	}
	public int getHeightAt(float x, float z){
		return worldGen.getHeightAt(x, z);
	}
	public void initalize(SinglePlayerGame game){
		if(created){}else{
			worldGen = new WorldGenerator();
			LoadingScreen loadingScreen = new LoadingScreen(new LoadingScreenTask(){
				int size = 300;
				int x, y, z, h;
				int columns = 0;
				int lastPercent = 0;
				public boolean runStep(){
					for(int i = 0; i<1024; i++){
						placeColumn();
						if(nextBlock()){
							updatePercent(100, "Loading world");
							placeSphere();
							rebuildAllChunks();
							buildShadowTexture();
							return true;
						}
					}
					columns += 1024;
					updatePercent(columns/(double)((size+1)*(size+1))*100, "Loading world");
					return false;
				}
				private void buildShadowTexture(){
					BufferedImage buf = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
					int[] rgb = new int[1024*1024];
					int x, y;
					for(x = 0; x<size; x++)
						for(y = 0; y<size; y++)
							rgb[y*1024+x] = (255<<24)+getHeighestBlockAt(x, y);
					buf.setRGB(0, 0, 1024, 1024, rgb, 0, 1024);
					try{
						ImageIO.write(buf, "PNG", new File(
							"C:/Documents and Settings/TheDudeFromCI/Desktop/ShadowMap.png"));
					}catch(IOException e){
						e.printStackTrace();
					}
					shadowTexture = new Texture(buf);
				}
				private int getHeighestBlockAt(int x, int z){
					int h = worldGen.getHeightAt(x, z);
					all:for(int i = 0; i<sphereLocations.size(); i++){
						if(Math.pow(x-sphereLocations.get(i)[0], 2)+Math.pow(z-sphereLocations.get(i)[1], 2)<=sphereLocations
							.get(i)[2])
							for(h = 255; h>=0; h--)
								// Should never hit 0. :P
								if(getBlock(x, h, z, false)!=null)
									break all;
					}
					return h;
				}
				private boolean nextBlock(){
					z++;
					if(z>size){
						z = 0;
						x++;
						if(x>size)
							return true;
					}
					return false;
				}
				private void placeColumn(){
					h = worldGen.getHeightAt(x, z);
					for(y = 0; y<h; y++)
						setBlock(x, y, z, Block.GRASS, false);
				}
				private void placeSphere(){
					int sphereCount = (int)(Math.random()*15+5);
					int x, y, z, h, i;
					for(i = 0; i<sphereCount; i++){
						int[] sizes = new int[3];
						sphereLocations.add(sizes);
						sizes[0] = (int)(Math.random()*size);
						sizes[1] = (int)(Math.random()*size);
						sizes[2] = (int)(Math.random()*16)+5;
						h = worldGen.getHeightAt(sizes[0], sizes[1])+sizes[2]+10;
						for(x = -sizes[2]; x<=sizes[2]; x++)
							for(y = -sizes[2]; y<=sizes[2]; y++)
								for(z = -sizes[2]; z<=sizes[2]; z++)
									if(x+sizes[0]>=0&&z+sizes[1]>=0&&x+sizes[0]<=size&&z+sizes[1]<=size
									&&x*x+y*y+z*z<=sizes[2]*sizes[2])
										setBlock(x+sizes[0], y+h, z+sizes[1], Block.DIRT, false);
						sizes[2] *= sizes[2];
					}
				}
				private void rebuild(Voxel vox){
					if(vox.isSolid()){
						if(vox.getState()!=Voxel.DEFAULT)
							((Chunk)vox.getState()).rebuild();
						return;
					}
					for(int i = 0; i<8; i++)
						rebuild(vox.getVoxel(i));
				}
				private void rebuildAllChunks(){
					lastPercent = -1;
					for(int i = 0; i<world.voxels.size(); i++){
						rebuild(world.voxels.get(i));
						updatePercent(i/(double)world.voxels.size()*100, "Rebuilding chunks");
					}
					updatePercent(100, "Rebuilding chunks");
					needsRebuffer = true;
				}
				private void updatePercent(double realPercent, String msg){
					int percent = (int)realPercent;
					if(lastPercent!=percent){
						lastPercent = percent;
						System.out.println(msg+"... "+lastPercent+"%");
					}
				}
			}, game);
			WraithavensConquest.INSTANCE.setDriver(loadingScreen);
		}
	}
	public void setNeedsRebuffer(){
		needsRebuffer = true;
	}
	private void dispose(Voxel vox){
		if(vox.isSolid()){
			if(vox.getState()!=Voxel.DEFAULT)
				((Chunk)vox.getState()).clearAllBatches();
			return;
		}
		for(int i = 0; i<8; i++)
			dispose(vox.getVoxel(i));
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
									}else
										chunk = (Chunk)c;
									chunk.setBlock(x, y, z, block);
									if(rebuild){
										chunk.rebuild();
										needsRebuffer = true;
									}
	}
	Block getBlock(int x, int y, int z, boolean load){
		int chunkX = x>>Chunk.CHUNK_BITS;
						int chunkY = y>>Chunk.CHUNK_BITS;
		int chunkZ = z>>Chunk.CHUNK_BITS;
		EmptyChunk c = world.getSubVoxelAt(chunkX, chunkY, chunkZ, true).getState();
		Chunk chunk;
		if(c==Voxel.DEFAULT){
			if(!load)
				return null;
			chunk = new Chunk(chunkX, chunkY, chunkZ, this);
			world.fillArea(chunkX, chunkY, chunkZ, 1, 1, 1, chunk);
		}else
			chunk = (Chunk)c;
		return chunk.getBlock(x, y, z);
	}
	VoxelWorld getWorldStorage(){
		return world;
	}
	boolean needsRebuffer(){
		return needsRebuffer;
	}
	void setRebuffered(){
		needsRebuffer = false;
	}
}
