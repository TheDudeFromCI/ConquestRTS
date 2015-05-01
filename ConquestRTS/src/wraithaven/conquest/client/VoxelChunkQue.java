package wraithaven.conquest.client;

import wraith.library.LWJGL.Voxel.BlockType;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.RandomGeneration.CosineInterpolation;
import wraith.library.RandomGeneration.NoiseGenerator;

public class VoxelChunkQue{
	private int x, y, z, h;
	public double tempDistance;
	public final VoxelChunk chunk;
	public final VoxelWorld world;
	public static BlockType type;
	private static final NoiseGenerator noise = new NoiseGenerator((long)(Math.random()*Integer.MAX_VALUE), 200, 3);
	public VoxelChunkQue(VoxelWorld world, VoxelChunk chunk){
		this.world=world;
		this.chunk=chunk;
		x=chunk.startX;
		y=chunk.startY;
		z=chunk.startZ;
	}
	public boolean update(){
		if(chunk.chunkY<0)return true;
		if(chunk.chunkY>CatcheChunkLoader.CHUNK_HEIGHT)return true;
		h=(int)Math.min(noise.noise(x, z)*CatcheChunkLoader.WORLD_HEIGHT, chunk.endY);
		for(y=chunk.startY; y<=h; y++)chunk.createBlock(x, y, z, type).getQuad(2).setRotation((int)(Math.random()*4));
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
	public static void setupTextures(VoxelWorld world){
		noise.setFunction(new CosineInterpolation());
		type=new BasicBlock(BlockTextures.grass.getTextures(), world);
	}
}