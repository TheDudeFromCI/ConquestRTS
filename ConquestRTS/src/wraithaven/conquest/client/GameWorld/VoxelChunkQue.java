package wraithaven.conquest.client.GameWorld;

import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.BlockType;
import wraithaven.conquest.client.GameWorld.Voxel.VoxelWorld;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;

class VoxelChunkQue{
	private int x, y, z, h;
	double tempDistance;
	final Chunk chunk;
	private final VoxelWorld world;
	private static BlockType type;
	private static final NoiseGenerator noise = new NoiseGenerator((long)(Math.random()*Integer.MAX_VALUE), 200, 3);
	VoxelChunkQue(VoxelWorld world, Chunk chunk){
		this.world=world;
		this.chunk=chunk;
		x=chunk.startX;
		y=chunk.startY;
		z=chunk.startZ;
	}
	boolean update(){
		if(chunk.chunkY<0)return true;
		if(chunk.chunkY>CatcheChunkLoader.CHUNK_HEIGHT)return true;
		h=(int)Math.min(noise.noise(x, z)*CatcheChunkLoader.WORLD_HEIGHT, chunk.endY);
		for(y=chunk.startY; y<=h; y++)chunk.createBlock(x, y, z, type);
		if(next()){
			optimize();
			return true;
		}
		return false;
	}
	private void optimize(){
		chunk.optimize();
		optimizeNearbyChunks(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
	}
	private boolean next(){
		z++;
		if(z>chunk.endZ){
			z=chunk.startZ;
			x++;
			if(x>chunk.endX)return true;
		}
		return false;
	}
	private void optimizeNearbyChunks(int chunkX, int chunkY, int chunkZ){
		Chunk chunk;
		chunk=world.getChunk(chunkX-1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(0);
		chunk=world.getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1);
		chunk=world.getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4);
		chunk=world.getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5);
	}
	static void setupTextures(VoxelWorld world){
		noise.setFunction(new CosineInterpolation());
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
		type=new BasicBlock(textures, world);
	}
}