package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.nio.FloatBuffer;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockType;

public class FloatingBlockType implements BlockType{
	private final CubeTextures textures;
	public FloatingBlockType(CubeTextures textures){ this.textures=textures; }
	public Texture getTexture(int side){ return textures.getTexture(side); }
	public int getRotation(int side){ return textures.getRotation(side); }
	public boolean setupShadows(FloatBuffer dataArray, int side, int x, int y, int z){ return false; }
	
}