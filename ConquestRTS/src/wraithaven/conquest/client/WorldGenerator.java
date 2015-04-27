package wraithaven.conquest.client;

import wraith.library.LWJGL.Camera;
import wraith.library.LWJGL.CubeTextures;
import wraith.library.LWJGL.Texture;
import wraith.library.LWJGL.Voxel.BlockType;
import wraith.library.LWJGL.Voxel.VoxelChunk;
import wraith.library.LWJGL.Voxel.VoxelWorld;
import wraith.library.LWJGL.Voxel.VoxelWorldListener;
import wraith.library.RandomGeneration.CosineInterpolation;
import wraith.library.RandomGeneration.NoiseGenerator;

public class WorldGenerator implements VoxelWorldListener{
	int x, y, z, h;
	private VoxelWorld voxelWorld;
	private final NoiseGenerator noise;
	private final BlockType type;
	private final Camera cam;
	public static final int WORLD_HEIGHT = 40;
	public static final int CAMERA_RADIUS = 5;
	public static final int CHUNK_HEIGHT = WORLD_HEIGHT>>4;
	public WorldGenerator(Camera cam){
		this.cam=cam;
		noise=new NoiseGenerator((long)(Math.random()*Integer.MAX_VALUE), 200, 3);
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
	public void loadChunk(VoxelChunk chunk){
		if(chunk.chunkY<0)return;
		if(chunk.chunkY>CHUNK_HEIGHT)return;
		for(x=chunk.startX; x<=chunk.endX; x++){
			for(z=chunk.startZ; z<=chunk.endZ; z++){
				h=(int)calculateHeight(x, z, chunk);
				for(y=chunk.startY; y<=h; y++)chunk.createBlock(x, y, z, type);
			}
		}
		chunk.optimize(true);
		optimizeNearbyChunks(chunk.chunkX, chunk.chunkY, chunk.chunkZ);
	}
	private float calculateHeight(int x, int z, VoxelChunk chunk){ return Math.min(noise.noise(x, z)*WORLD_HEIGHT, chunk.endY); }
	private void optimizeNearbyChunks(int chunkX, int chunkY, int chunkZ){
		VoxelChunk chunk;
		chunk=voxelWorld.getChunk(chunkX-1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(0, true);
		chunk=voxelWorld.getChunk(chunkX+1, chunkY, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(1, true);
		chunk=voxelWorld.getChunk(chunkX, chunkY-1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(2, true);
		chunk=voxelWorld.getChunk(chunkX, chunkY+1, chunkZ, false);
		if(chunk!=null)chunk.optimizeSide(3, true);
		chunk=voxelWorld.getChunk(chunkX, chunkY, chunkZ-1, false);
		if(chunk!=null)chunk.optimizeSide(4, true);
		chunk=voxelWorld.getChunk(chunkX, chunkY, chunkZ+1, false);
		if(chunk!=null)chunk.optimizeSide(5, true);
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return Math.pow(chunk.chunkX-((int)cam.x>>4), 2)+Math.pow(chunk.chunkY-((int)cam.y>>4), 2)+Math.pow(chunk.chunkZ-((int)cam.z>>4), 2)<25; }
	public void setWorld(VoxelWorld voxelWorld){ this.voxelWorld=voxelWorld; }
	public void unloadChunk(VoxelChunk chunk){}
}