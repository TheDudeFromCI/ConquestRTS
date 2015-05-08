package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public interface ChunklessBlockHolder{
	public QuadBatch getBatch(Texture texture);
}