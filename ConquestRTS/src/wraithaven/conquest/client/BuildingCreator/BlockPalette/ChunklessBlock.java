package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.CustomBlock;

public class ChunklessBlock extends CustomBlock{
	private final ChunklessBlockHolder holder;
	public ChunklessBlock(ChunklessBlockHolder holder, BlockShape shape, CubeTextures textures, BlockRotation rotation){
		super(null, 0, 0, 0, new FloatingBlockType(textures), shape, textures, rotation);
		this.holder=holder;
	}
	@Override protected boolean getSubBlock(int x, int y, int z){
		if(x<0||x>7||y<0||y>7||z<0||z>7)return false;
		return shape.getBlock(x, y, z, rotation);
	}
	public void optimizeSide(int side){
		super.optimizeSide(side);
		if(shape.fullSide(side, rotation))calculateColors(quads[side].data, side);
	}
	@Override public Block getTouchingBlock(int side){ return null; }
	@Override protected QuadBatch getBatch(Texture texture){ return holder.getBatch(texture); }
	@Override protected QuadBatch getLargeBatch(Texture texture){ return holder.getBatch(texture); }
}