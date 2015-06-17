package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.Block;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;

public class IndexManager{
	public static final short AIR_BLOCK = Short.MIN_VALUE;
	private static final short INDEX_SHIFT = (short)Math.abs(Short.MIN_VALUE+1);
	public final ArrayList<Block> blocks = new ArrayList();
	public Block getBlock(int index){
		return blocks.get(index+INDEX_SHIFT);
	}
	public short indexOf(BlockShape shape, CubeTextures textures, BlockRotation rot){
		for(int i = 0; i<blocks.size(); i++)
			if(blocks.get(i).shape==shape
				&&blocks.get(i).originalCubeTextures==textures
				&&blocks.get(i).rotation==rot)return (short)(i-INDEX_SHIFT);
		Block block = new Block(shape, textures, rot);
		blocks.add(block);
		return (short)(blocks.size()-1-INDEX_SHIFT);
	}
}