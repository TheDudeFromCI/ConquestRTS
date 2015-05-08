package wraithaven.conquest.client.BuildingCreator.BlockPalette;

import java.util.ArrayList;
import wraithaven.conquest.client.GameWorld.Voxel.Texture;
import wraithaven.conquest.client.GameWorld.Voxel.BlockShape;
import wraithaven.conquest.client.GameWorld.Voxel.CubeTextures;
import wraithaven.conquest.client.GameWorld.Voxel.QuadBatch;
import static org.lwjgl.opengl.GL11.*;

public class BlockIcon implements ChunklessBlockHolder{
	private int itemSlot;
	private float shiftRX, shiftRY;
	private float x, y, z;
	private float rx, ry, tempY;
	private final ArrayList<QuadBatch> batches = new ArrayList();
	private final ChunklessBlock block;
	private static final float BLOCK_BOB_SPEED = 0.6f;
	private static final float BLOCK_BOB_DISTANCE = 8;
	private static final float BLOCK_HOVER_SPEED = 0.25f;
	private static final float BLOCK_HOVER_DISTANCE = 0.1f;
	public BlockIcon(BlockShape shape, CubeTextures textures){
		block=new ChunklessBlock(this, shape, textures);
		block.build();
		for(int i = 0; i<batches.size(); i++)batches.get(i).recompileBuffer();
	}
	public void update(double time){
		rx=(float)Math.cos(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+30;
		ry=(float)Math.sin(time*BLOCK_BOB_SPEED)*BLOCK_BOB_DISTANCE+45;
		tempY=(float)Math.sin(time*BLOCK_HOVER_SPEED*0.9f)*BLOCK_HOVER_DISTANCE;
	}
	public void render(){
		x=getX(itemSlot);
		y=getY(itemSlot);
		glPushMatrix();
		glTranslatef(x, y+tempY, z);
		glRotatef(rx+shiftRX, 1, 0, 0);
		glRotatef(ry+shiftRY, 0, 1, 0);
		glTranslatef(-0.5f, -0.5f, -0.5f);
		for(int i = 0; i<batches.size(); i++){
			batches.get(i).getTexture().bind();
			batches.get(i).renderPart();
		}
		glPopMatrix();
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
	private static float getX(int id){
		return 0;
	}
	private static float getY(int id){
		return 0;
	}
}