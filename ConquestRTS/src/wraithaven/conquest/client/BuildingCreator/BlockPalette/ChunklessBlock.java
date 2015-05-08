package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.CustomBlock;

public class ChunklessBlock extends CustomBlock{
	private final ChunklessBlockHolder holder;
	ChunklessBlock(ChunklessBlockHolder holder, BlockShape shape, CubeTextures textures){
		super(null, 0, 0, 0, new FloatingBlockType(textures), shape, textures);
		this.holder=holder;
	}
	@Override protected boolean getSubBlock(int x, int y, int z){
		if(x<0||x>7||y<0||y>7||z<0||z>7)return false;
		return shape.getBlock(x, y, z);
	}
	@Override public Block getTouchingBlock(int side){ return null; }
	@Override protected QuadBatch getBatch(Texture texture){ return holder.getBatch(texture); }
}