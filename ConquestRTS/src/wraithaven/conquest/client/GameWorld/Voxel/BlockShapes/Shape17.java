package wraithaven.conquest.client.GameWorld.Voxel.BlockShapes;

import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;

public class Shape17 extends BlockShape{
	private static final boolean[] BLOCKS = {
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		false, false, false, false, false, false, false, false,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, true, true, true, true,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
		true, true, true, true, false, false, false, false,
	};
	Shape17(){}
	protected boolean[] getBlocks(){ return BLOCKS; }
	protected boolean hasFullSide(int side){ return false; }
}