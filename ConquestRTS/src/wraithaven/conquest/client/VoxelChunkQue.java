package wraithaven.conquest.client;

import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;
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
	private static BlockType type;
	private static final NoiseGenerator noise = new NoiseGenerator((long)(Math.random()*Integer.MAX_VALUE), 200, 3);
	public static final int WORLD_HEIGHT = 40;
	public static final int CAMERA_RADIUS = 5;
	public static final int CHUNK_HEIGHT = WORLD_HEIGHT>>4;
	public VoxelChunkQue(VoxelWorld world, VoxelChunk chunk){
		this.world=world;
		this.chunk=chunk;
		x=chunk.startX;
		y=chunk.startY;
		z=chunk.startZ;
	}
	public boolean update(){
		if(chunk.chunkY<0)return true;
		if(chunk.chunkY>CHUNK_HEIGHT)return true;
		h=(int)Math.min(noise.noise(x, z)*WORLD_HEIGHT, chunk.endY);
		for(y=chunk.startY; y<=h; y++)chunk.createBlock(x, y, z, type);
		if(next()){
			optimize();
			return true;
		}
		return false;
	}
	private void optimize(){
		chunk.optimize(true);
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
		if(chunk!=null)chunk.optimizeSide(0, true);
		chunk=world.getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1, true);
		chunk=world.getChunk(chunkX, chunkY-1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(2, true);
		chunk=world.getChunk(chunkX, chunkY+1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(3, true);
		chunk=world.getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4, true);
		chunk=world.getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5, true);
	}
	public static void setupTextures(){
		noise.setFunction(new CosineInterpolation());
		final CubeTextures cubeTextures = BlockTextures.grass.getTextures();
		type=new BlockType(){
			public Texture getTexture(int side){
				if(side==0)return cubeTextures.xUp;
				if(side==1)return cubeTextures.xDown;
				if(side==2)return cubeTextures.yUp;
				if(side==3)return cubeTextures.yDown;
				if(side==4)return cubeTextures.zUp;
				if(side==5)return cubeTextures.zDown;
				return null;
			}
		};
	}
}