package wraithaven.conquest.client.BuildingCreator;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;
import wraithaven.conquest.client.GameWorld.BasicBlock;
import wraithaven.conquest.client.GameWorld.BlockTextures;

public class BuildCreatorWorld implements VoxelWorldListener{
	private Camera camera;
	private BasicBlock block;
	private VoxelWorld world;
	public void setup(VoxelWorld world, Camera camera){
		this.camera=camera;
		this.world=world;
		CubeTextures textures = new CubeTextures();
		textures.xUp=BlockTextures.sideDirt.getTexture();
		textures.xUpRotation=0;
		textures.xDown=BlockTextures.sideDirt.getTexture();
		textures.xDownRotation=1;
		textures.yUp=BlockTextures.grass.getTexture();
		textures.yUpRotation=0;
		textures.yDown=BlockTextures.dirt.getTexture();
		textures.yDownRotation=0;
		textures.zUp=BlockTextures.sideDirt.getTexture();
		textures.zUpRotation=3;
		textures.zDown=BlockTextures.sideDirt.getTexture();
		textures.zDownRotation=2;
		block=new BasicBlock(textures, world);
	}
	public void loadChunk(VoxelChunk chunk){
		if(chunk.chunkY==0){
			int x, z;
			for(x=chunk.startX; x<=chunk.endX; x++)for(z=chunk.startZ; z<=chunk.endZ; z++)chunk.createBlock(x, 0, z, block).getQuad(2).setRotation((int)(Math.random()*4));
			chunk.optimize();
			optimizeNearbyChunks(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
		}
	}
	private void optimizeNearbyChunks(int chunkX, int chunkY, int chunkZ){
		VoxelChunk chunk;
		chunk=world.getChunk(chunkX-1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(0);
		chunk=world.getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1);
		chunk=world.getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4);
		chunk=world.getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5);
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return !chunk.isHidden()&&camera.frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, 16); }
	public void unloadChunk(VoxelChunk chunk){}
}