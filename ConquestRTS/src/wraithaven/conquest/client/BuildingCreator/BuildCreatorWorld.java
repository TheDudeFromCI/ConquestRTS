package wraithaven.conquest.client.BuildingCreator;

import wraithaven.conquest.client.ClientLauncher;
import wraithaven.conquest.client.GameWorld.LoopControls.VoxelWorldListener;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.Chunk;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.MipmapQuality;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShapes.ShapeType;

public class BuildCreatorWorld implements VoxelWorldListener{
	private static short index;
	public static void setup(){
		CubeTextures textures = new CubeTextures();
		Texture grass = Texture.getTexture(ClientLauncher.textureFolder, "Grass.png", 4, MipmapQuality.HIGH);
		textures.xUp = grass;
		textures.xDown = grass;
		textures.yUp = grass;
		textures.yDown = grass;
		textures.zUp = grass;
		textures.zDown = grass;
		index = Loop.INSTANCE.getVoxelWorld().indexOfBlock(ShapeType.SHAPE_0.shape, textures, BlockRotation.ROTATION_0);
	}
	public boolean isChunkVisible(Chunk chunk){
		return !chunk.isHidden()&&Loop.INSTANCE.getCamera().frustum.cubeInFrustum(chunk.startX, chunk.startY, chunk.startZ, Chunk.BLOCKS_PER_CHUNK);
	}
	public void loadChunk(Chunk chunk){
		if(chunk.chunkY==0){
			int x, z;
			for(x = chunk.startX; x<=chunk.endX; x++)
				for(z = chunk.startZ; z<=chunk.endZ; z++)
					chunk.setBlock(x, 0, z, index);
		}
	}
	public void unloadChunk(Chunk chunk){}
}