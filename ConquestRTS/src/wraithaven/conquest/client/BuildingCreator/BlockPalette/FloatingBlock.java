package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;

public class FloatingBlock implements ChunklessBlockHolder{
	final ArrayList<QuadBatch> batches = new ArrayList();
	ChunklessBlock block;
	float rx,
			ry,
			tempY;
	float shiftRX,
			shiftRY;
	float x,
			y,
			z;
	void destroy(){
		block = null;
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).cleanUp();
		batches.clear();
	}
	public void dispose(){
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).cleanUp();
	}
	public QuadBatch getBatch(Texture texture){
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch = batches.get(i);
			if(batch.getTexture()==texture) return batch;
		}
		batch = new QuadBatch(texture, 0, 0, 0);
		batches.add(batch);
		return batch;
	}
	void rebuildBatches(){
		for(int i = 0; i<batches.size(); i++)
			batches.get(i).recompileBuffer();
	}
	public void render(){
		for(int i = 0; i<batches.size(); i++){
			batches.get(i).getTexture().bind();
			batches.get(i).renderPart();
		}
	}
}