package wraithaven.conquest.client.GameWorld.Voxel.BlockIndexing;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.BlockRotation;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.Block;

public class IndexManager{
	private final ArrayList<Block> blocks = new ArrayList();
	public void addBlock(Block block){ blocks.add(block); }
	public Block getBlock(int index){ return blocks.get(index); }
	public int getBlockCount(){ return blocks.size(); }
	public short indexOf(BlockShape shape, CubeTextures textures, BlockRotation rot){
		//TODO Shift all indices by +Math.abs(Short.MIN_VALUE+1)
		for(short i = 0; i<blocks.size(); i++){
			if(blocks.get(i).shape==shape&&blocks.get(i).originalCubeTextures==textures&&blocks.get(i).rotation==rot)return i;
		}
		Block block = new Block(shape, textures, rot);
		blocks.add(block);
		return (short)(blocks.size()-1);
	}
}