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
	public WorldGenerator(Camera cam){
		this.cam=cam;
		noise=new NoiseGenerator((long)(Math.random()*Integer.MAX_VALUE), 60, 2);
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
	public VoxelChunk loadChunk(int chunkX, int chunkY, int chunkZ){
		VoxelChunk chunk = new VoxelChunk(voxelWorld, chunkX, chunkY, chunkZ);
		for(x=0; x<16; x++){
			for(z=0; z<16; z++){
				h=Math.min((int)(noise.noise(x+chunk.startX, z+chunk.startZ)*WORLD_HEIGHT)-chunk.startY, 15);
				for(y=0; y<=h; y++)chunk.createBlock(x+chunk.startX, y+chunk.startY, z+chunk.startZ, type);
			}
		}
		chunk.optimize();
		return chunk;
	}
	public boolean isChunkVisible(VoxelChunk chunk){ return Math.pow(chunk.chunkX-((int)cam.x>>4), 2)+Math.pow(chunk.chunkY-((int)cam.y>>4), 2)+Math.pow(chunk.chunkZ-((int)cam.z>>4), 2)<25; }
	public void setWorld(VoxelWorld voxelWorld){ this.voxelWorld=voxelWorld; }
	public void unloadChunk(VoxelChunk chunk){}
}