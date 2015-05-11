package wraithaven.conquest.client.GameWorld.Voxel.BlockShapes;

import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;

public class Cube extends BlockShape{
	private static final boolean[] BLOCKS = {
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
	};
	protected boolean[] getBlocks(){ return BLOCKS; }
	public boolean fullSide(int side){ return true; }
}