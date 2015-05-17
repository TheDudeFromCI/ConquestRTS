package wraithaven.conquest.client.BuildingCreator;

import java.io.File;
import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldListener;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.Camera;
import wraithaven.conquest.client.GameWorld.BasicBlock;

public class BuildCreatorWorld implements VoxelWorldListener{
	private Camera camera;
	private BasicBlock block;
	private VoxelWorld world;
	public void setup(VoxelWorld world, Camera camera){
		this.camera=camera;
		this.world=world;
		CubeTextures textures = new CubeTextures();
		//TODO Get this texture from a list of already loaded textures to avoid the grass texture from being loaded twice.
		Texture grass = new Texture(new File(ClientLauncher.textureFolder, "Grass.png"), 4, MipmapQuality.HIGH);
		textures.xUp=grass;
		textures.xDown=grass;
		textures.yUp=grass;
		textures.yDown=grass;
		textures.zUp=grass;
		textures.zDown=grass;
		block=new BasicBlock(textures, world);
	}
	public void loadChunk(Chunk chunk){
		if(chunk.chunkY==0){
			int x, z;
			for(x=chunk.startX; x<=chunk.endX; x++)for(z=chunk.startZ; z<=chunk.endZ; z++)chunk.createBlock(x, 0, z, block);
			chunk.optimize();
			optimizeNearbyChunks(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
		}
	}
	private void optimizeNearbyChunks(int chunkX, int chunkY, int chunkZ){
		Chunk chunk;
		chunk=world.getChunk(chunkX-1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(0);
		chunk=world.getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1);
		chunk=world.getChunk(chunkX, chunkY+1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(2);
		chunk=world.getChunk(chunkX, chunkY-1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(3);
		chunk=world.getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4);
		chunk=world.getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5);
	}
	public boolean isChunkVisible(Chunk chunk){ return !chunk.isHidden()&&camera.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, Chunk.BLOCKS_PER_CHUNK); }
	public void unloadChunk(Chunk chunk){}
}