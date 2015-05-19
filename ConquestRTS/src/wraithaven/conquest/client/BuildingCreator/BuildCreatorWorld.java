package wraithaven.conquest.client.BuildingCreator;

import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldListener;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class BuildCreatorWorld implements VoxelWorldListener{
	public static void setup(){
		CubeTextures textures = new CubeTextures();
		Texture grass = Texture.getTexture(ClientLauncher.textureFolder, "Grass.png", 4, MipmapQuality.HIGH);
		textures.xUp=grass;
		textures.xDown=grass;
		textures.yUp=grass;
		textures.yDown=grass;
		textures.zUp=grass;
		textures.zDown=grass;
	}
	public void loadChunk(Chunk chunk){
		if(chunk.chunkY==0){
			int x, z;
			for(x=chunk.startX; x<=chunk.endX; x++)for(z=chunk.startZ; z<=chunk.endZ; z++)chunk.createBlock(x, 0, z);
			chunk.optimize();
			optimizeNearbyChunks(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
		}
	}
	private static void optimizeNearbyChunks(int chunkX, int chunkY, int chunkZ){
		Chunk chunk;
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX-1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(0);
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1);
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX, chunkY+1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(2);
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX, chunkY-1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(3);
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4);
		chunk=Loop.INSTANCE.getVoxelWorld().getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5);
	}
	public boolean isChunkVisible(Chunk chunk){ return !chunk.isHidden()&&Loop.INSTANCE.getCamera().frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, Chunk.BLOCKS_PER_CHUNK); }
	public void unloadChunk(Chunk chunk){}
}