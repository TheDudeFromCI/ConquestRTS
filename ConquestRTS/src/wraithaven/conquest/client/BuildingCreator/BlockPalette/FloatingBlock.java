package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import static org.lwjgl.opengl.GL11.*;

public class FloatingBlock implements ChunklessBlockHolder{
	float shiftRX, shiftRY;
	float x, y, z;
	ChunklessBlock block;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private float rx, ry, tempY;
	private static final float BLOCK_BOB_SPEED = 0.6f;
	private static final float BLOCK_BOB_DISTANCE = 8;
	private static final float BLOCK_HOVER_SPEED = 0.25f;
	private static final float BLOCK_HOVER_DISTANCE = 0.1f;
	public void render(){
		glTranslatef(x, y+tempY, z);
		glRotatef(rx+shiftRX, 1, 0, 0);
		glRotatef(ry+shiftRY, 0, 1, 0);
		glTranslatef(-0.5f, -0.5f, -0.5f);
		for(int i = 0; i<batches.size(); i++){
			batches.get(i).getTexture().bind();
			batches.get(i).renderPart();
		}
	}
	public void update(double time){
		rx=(float)Math.cos(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+30;
		ry=(float)Math.sin(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+45;
		tempY=(float)Math.sin(time*BLOCK_HOVER_SPEED*0.9f)*BLOCK_HOVER_DISTANCE;
	}
	public QuadBatch getBatch(Texture texture){
		QuadBatch batch;
		for(int i = 0; i<batches.size(); i++){
			batch=batches.get(i);
			if(batch.getTexture()==texture)return batch;
		}
		batch=new QuadBatch(texture, true, 0, 0, 0);
		batches.add(batch);
		return batch;
	}
	void rebuildBatches(){ for(int i = 0; i<batches.size(); i++)batches.get(i).recompileBuffer(); }
}